package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;

import com.mycompany.quanlynongsan.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private final UserRepository userRepo = new UserRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");

        try {
            userRepo.updatePassword(email, newPassword);

            String message = "Đặt lại mật khẩu thành công. Vui lòng đăng nhập.";
            response.sendRedirect(
                request.getContextPath() + "/login.jsp?success=" + URLEncoder.encode(message, "UTF-8")
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            String error = "Đã xảy ra lỗi khi đặt lại mật khẩu. Vui lòng thử lại.";
            response.sendRedirect(
                request.getContextPath() + "/reset-password.jsp?email=" + email + 
                "&error=" + URLEncoder.encode(error, "UTF-8")
            );
        }
    }
}
