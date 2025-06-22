/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;

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
            String message = "Đặt lại mật khẩu thành công";
            response.sendRedirect(request.getContextPath() + "/login?success=" + URLEncoder.encode(message, "UTF-8"));

        } catch (Exception ex) {
            ex.printStackTrace();
            String message = "Đã xảy ra lỗi";
            response.sendRedirect(request.getContextPath() + "/reset-password.jsp?email=" + email + "&error="
                    + URLEncoder.encode(message, "UTF-8"));
        }
    }
}
