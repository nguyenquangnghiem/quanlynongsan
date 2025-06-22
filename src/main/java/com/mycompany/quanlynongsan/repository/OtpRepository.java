/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.dao.EmailService;

import jakarta.mail.MessagingException;

/**
 *
 * @author nghiem
 */
public class OtpRepository {

    public OtpRepository() {
    }

    public void createAndSendOtp(String email) throws SQLException {
        String otp = String.format("%06d", new Random().nextInt(999999));
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn
                        .prepareStatement("INSERT INTO OTP (email, code, created_date) VALUES (?, ?, GETDATE())")) {
            ps.setString(1, email);
            ps.setString(2, otp);
            ps.executeUpdate();
        }
        sendEmail(email, otp);
    }

    public boolean verifyOtp(String email, String otp) throws SQLException {
        String sql = "SELECT COUNT(*) FROM OTP WHERE email = ? AND code = ? AND created_date >= DATEADD(MINUTE, -10, GETDATE())";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, otp);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private void sendEmail(String email, String otp) {
        try {
            new EmailService().sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
