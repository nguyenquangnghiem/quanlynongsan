/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;

import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;

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

@WebServlet(urlPatterns = { "/logout" })
public class LogoutServlet extends HttpServlet {

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false); // Không tạo mới nếu chưa có
        User user = (User) session.getAttribute("user");
        if (session != null) {
            session.invalidate(); // Hủy session hiện tại
        }
        Behavior behavior = behaviorRepository.findByCode("LOGOUT");
        behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());

        resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
