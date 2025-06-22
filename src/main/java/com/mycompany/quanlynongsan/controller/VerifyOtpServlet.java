/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;

import com.mycompany.quanlynongsan.repository.OtpRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author nghiem
 */
@WebServlet("/verify-otp")
public class VerifyOtpServlet extends HttpServlet {

    private final OtpRepository otpRepo = new OtpRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String otp = request.getParameter("otp");

        try {
            boolean isValid = otpRepo.verifyOtp(email, otp);

            if (isValid) {
                response.sendRedirect(request.getContextPath() + "/reset-password.jsp?email=" + email);
            } else {
                String message = "Mã OTP không hợp lệ";
                response.sendRedirect(request.getContextPath() + "/verify-otp.jsp?email=" + email + "&error="
                        + URLEncoder.encode(message, "UTF-8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "Lỗi xác thực OTP";
            response.sendRedirect(request.getContextPath() + "/verify-otp.jsp?email=" + email + "&error="
                    + URLEncoder.encode(message, "UTF-8"));
        }
    }
}
