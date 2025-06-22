/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.util.List;

import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.DeliveryRepository;
import com.mycompany.quanlynongsan.repository.OrderProductRepository;
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
@WebServlet(urlPatterns = "/secured/user/update-order-status")
public class UpdateOrderStatus extends HttpServlet {

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/update-order-status.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            String status = req.getParameter("status"); // SUCCESSFUL / CANCELED / RETURNED
            String newHistory = req.getParameter("newHistory"); // ô nhập thêm lịch sử mới

            OrderRepository orderRepository = new OrderRepository();
            DeliveryRepository deliveryRepository = new DeliveryRepository();

            // ✅ 1. Cập nhật trạng thái đơn hàng
            orderRepository.updateStatus(orderId, status, user);

            // ✅ 2. Nếu có nhập thêm lịch sử vận chuyển → thêm vào bảng DELIVERY
            if (newHistory != null && !newHistory.trim().isEmpty()) {
                deliveryRepository.insert(orderId, newHistory.trim());
                Behavior behavior = behaviorRepository.findByCode("ADD_DELIVERY_HISTORY");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
            }

            // ✅ 3. Chuyển hướng về trang chi tiết đơn hoặc danh sách đơn hàng
            OrderProductRepository repo = new OrderProductRepository();
            List<OrderProductRepository.OrderSummary> orders = repo
                    .getAllRelatedOrderSummariesByUserId(user.getUserId());
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/user/my-order.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cập nhật đơn hàng thất bại.");
        }
    }

}
