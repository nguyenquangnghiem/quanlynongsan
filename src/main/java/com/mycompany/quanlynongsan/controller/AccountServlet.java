/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.util.List;

import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.UserRepository;
import com.mycompany.quanlynongsan.response.UserWithRole;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author nghiem
 */

@WebServlet(urlPatterns = "/secured/admin/account")
public class AccountServlet extends HttpServlet {

    private UserRepository userRepository = new UserRepository();

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String roleIdParam = req.getParameter("roleId"); // 🔔 lấy roleId từ URL nếu có
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String success = req.getParameter("success");
        String error = req.getParameter("error");
        if (success != null) {
            success = java.net.URLDecoder.decode(success, "UTF-8");
            req.setAttribute("success", success);
        }
        if (error != null) {
            error = java.net.URLDecoder.decode(error, "UTF-8");
            req.setAttribute("error", error);
        }
        List<UserWithRole> users;
        if (roleIdParam != null && !roleIdParam.isBlank()) {
            try {
                int roleId = Integer.parseInt(roleIdParam);
                users = userRepository.findAllWithRole(roleId); // Lấy theo role cụ thể
                req.setAttribute("selectedRoleId", roleId); // Để hiện trong dropdown chọn
            } catch (NumberFormatException e) {
                users = userRepository.findAllWithRole(); // Nếu lỗi parse, lấy tất cả
            }
        } else {
            users = userRepository.findAllWithRole(); // Không có filter → lấy tất cả
        }

        req.setAttribute("users", users);
        req.getRequestDispatcher("/admin/account.jsp").forward(req, resp);
    }
}
