/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;

import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.OtpRepository;
import com.mycompany.quanlynongsan.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author nghiem
 */
@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private final OtpRepository otpRepo = new OtpRepository();
    private final UserRepository userRepo = new UserRepository();
    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");

        try {
            User user = userRepo.findByEmail(email);

            if (user != null) {
                otpRepo.createAndSendOtp(email);
                Behavior behavior = behaviorRepository.findByCode("RESET_PASSWORD");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                response.sendRedirect(request.getContextPath() + "/verify-otp.jsp?email=" + email);
            } else {
                String message = "Email không tồn tại";
                response.sendRedirect(
                        request.getContextPath() + "/forgot-password?error=" + URLEncoder.encode(message, "UTF-8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "Đã xảy ra lỗi";
            response.sendRedirect(
                    request.getContextPath() + "/forgot-password?error=" + URLEncoder.encode(message, "UTF-8"));
        }
    }
}
