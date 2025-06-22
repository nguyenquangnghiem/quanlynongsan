/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.util.List;

import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.OrderProductRepository;

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

@WebServlet(urlPatterns = "/secured/user/my-order")
public class MyOrderServlet extends HttpServlet {

    private OrderProductRepository orderProductRepository = new OrderProductRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        OrderProductRepository repo = new OrderProductRepository();
        List<OrderProductRepository.OrderSummary> orders = repo.getOrderSummariesByUserId(user.getUserId());
        if (user.getRoleId() == 2) {
            orders = repo.getAllRelatedOrderSummariesByUserId(user.getUserId());
        }
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/user/my-order.jsp").forward(req, resp);
    }

}
