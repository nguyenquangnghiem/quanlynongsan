package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;
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

@WebServlet(urlPatterns = "/secured/user/update-order-status")
public class UpdateOrderStatus extends HttpServlet {

    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

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

            // ✅ 3. Chuyển hướng về danh sách đơn hàng kèm thông báo
            String successMsg = "Cập nhật đơn hàng thành công.";
            resp.sendRedirect(req.getContextPath() + "/user/my-order.jsp?success=" + URLEncoder.encode(successMsg, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = "Đã xảy ra lỗi khi cập nhật đơn hàng.";
            resp.sendRedirect(req.getContextPath() + "/user/my-order.jsp?error=" + URLEncoder.encode(errorMsg, "UTF-8"));
        }
    }
}
