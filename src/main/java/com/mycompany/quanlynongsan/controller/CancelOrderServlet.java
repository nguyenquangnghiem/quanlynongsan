/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.repository.OrderRepository;
import com.mycompany.quanlynongsan.model.Order;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "CancelOrderServlet", urlPatterns = {"/secured/user/cancel-order"})
public class CancelOrderServlet extends HttpServlet {

    private final OrderRepository orderRepository = new OrderRepository();
    
    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdParam = request.getParameter("orderId");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            int orderId = Integer.parseInt(orderIdParam);
            Order order = orderRepository.getById(orderId);

            if (order != null && "PENDING".equals(order.getStatus())) {
                orderRepository.updateStatus(orderId, "CANCELED");
                Behavior behavior = behaviorRepository.findByCode("CANCEL_ORDER");
            behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                response.sendRedirect(request.getContextPath() + "/secured/user/my-order?success=cancel");
            } else {
                // Không thể hủy vì không phải trạng thái PENDING
                response.sendRedirect(request.getContextPath() + "/secured/user/my-order?error=invalid_status");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/secured/user/my-order?error=exception");
        }
    }
}
