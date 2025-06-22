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

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private final OtpRepository otpRepo = new OtpRepository();
    private final UserRepository userRepo = new UserRepository();
    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

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

                String successMsg = URLEncoder.encode("Đã gửi mã OTP đến email của bạn!", "UTF-8");
                response.sendRedirect(request.getContextPath() + "/verify-otp.jsp?email=" + email + "&success=" + successMsg);
            } else {
                String message = URLEncoder.encode("❌ Email không tồn tại!", "UTF-8");
                response.sendRedirect(request.getContextPath() + "/forgot-password.jsp?error=" + message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = URLEncoder.encode("❌ Đã xảy ra lỗi khi gửi OTP!", "UTF-8");
            response.sendRedirect(request.getContextPath() + "/forgot-password.jsp?error=" + message);
        }
    }
}
