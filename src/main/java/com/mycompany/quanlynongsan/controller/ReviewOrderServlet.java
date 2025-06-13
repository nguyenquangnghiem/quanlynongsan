/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.repository.OrderRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author nghiem
 */

@WebServlet(urlPatterns = "/secured/review")
public class ReviewOrderServlet extends HttpServlet {
    
    private OrderRepository orderRepository = new OrderRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer orderId = Integer.valueOf(req.getParameter("orderId"));
        String comment = req.getParameter("comment");
        Integer rate = Integer.valueOf(req.getParameter("rate"));
        orderRepository.updateRateAndComment(orderId, rate, comment);
        req.getRequestDispatcher("/user/detail-order.jsp").forward(req, resp);
    }
    
}
