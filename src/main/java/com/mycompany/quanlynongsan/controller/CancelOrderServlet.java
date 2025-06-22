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
import java.net.URLEncoder;

@WebServlet(name = "CancelOrderServlet", urlPatterns = {"/secured/user/cancel-order"})
public class CancelOrderServlet extends HttpServlet {

    private final OrderRepository orderRepository = new OrderRepository();
    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdParam = request.getParameter("orderId");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String redirectURL = request.getContextPath() + "/secured/user/my-order";

        try {
            int orderId = Integer.parseInt(orderIdParam);
            Order order = orderRepository.getById(orderId);

            if (order != null && "PENDING".equals(order.getStatus())) {
                orderRepository.updateStatus(orderId, "CANCELED");
                Behavior behavior = behaviorRepository.findByCode("CANCEL_ORDER");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());

                String successMsg = URLEncoder.encode("Hủy đơn hàng thành công!", "UTF-8");
                response.sendRedirect(redirectURL + "?success=" + successMsg);
            } else {
                String errorMsg = URLEncoder.encode("Không thể hủy đơn hàng này!", "UTF-8");
                response.sendRedirect(redirectURL + "?error=" + errorMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = URLEncoder.encode("Đã xảy ra lỗi khi hủy đơn hàng!", "UTF-8");
            response.sendRedirect(redirectURL + "?error=" + errorMsg);
        }
    }
}
