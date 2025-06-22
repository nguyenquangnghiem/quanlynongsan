/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;

import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.OrderRepository;

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

@WebServlet(urlPatterns = "/secured/review")
public class ReviewOrderServlet extends HttpServlet {

    private OrderRepository orderRepository = new OrderRepository();

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false); // Không tạo mới nếu chưa có
        User user = (User) session.getAttribute("user");
        Integer orderId = Integer.valueOf(req.getParameter("orderId"));
        String comment = req.getParameter("comment");
        Integer rate = Integer.valueOf(req.getParameter("rate"));
        orderRepository.updateRateAndComment(orderId, rate, comment);
        Behavior behavior = behaviorRepository.findByCode("SUBMIT_REVIEW");
        behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
        req.getRequestDispatcher("/user/detail-order.jsp").forward(req, resp);
    }

}
