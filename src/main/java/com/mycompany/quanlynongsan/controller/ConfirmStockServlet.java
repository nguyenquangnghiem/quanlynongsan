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

@WebServlet(urlPatterns = "/confirm-stock")
public class ConfirmStockServlet extends HttpServlet {

    private OrderRepository orderRepository = new OrderRepository();

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer orderId = Integer.valueOf(req.getParameter("orderId"));
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        orderRepository.importProductsForUser(orderId, user.getUserId());
        Behavior behavior = behaviorRepository.findByCode("IMPORT_PRODUCT");
        behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
        req.getRequestDispatcher("/secured/user/orders-not-imported").forward(req, resp);
    }

}
