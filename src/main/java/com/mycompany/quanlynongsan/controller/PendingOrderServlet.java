package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import com.mycompany.quanlynongsan.dao.OrderDAO;
import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.Order;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.OrderRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/secured/user/pending-orders")
public class PendingOrderServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        String status = request.getParameter("status");

        if (status == null) status = "pending"; // fallback

        try {
            if (status.equals("pending")) {
                List<OrderRepository.OrderSummary> pendingOrders = OrderRepository
                        .getPendingOrdersBySeller(currentUser.getUserId());
                request.setAttribute("pendingOrders", pendingOrders);
                request.getRequestDispatcher("/user/pending-orders.jsp").forward(request, response);
            } else if (status.equals("confirmed")) {
                List<OrderRepository.OrderSummary> confirmedOrders = OrderRepository
                        .getConfirmedOrdersBySeller(currentUser.getUserId());
                request.setAttribute("confirmedOrders", confirmedOrders);
                request.getRequestDispatcher("/user/confirmed-orders.jsp").forward(request, response);
            } else if (status.equals("successful")) {
                List<OrderRepository.OrderSummary> successfulOrders = OrderRepository
                        .getSuccessfulOrdersBySeller(currentUser.getUserId());
                request.setAttribute("successfulOrders", successfulOrders);
                request.getRequestDispatcher("/user/successful-orders.jsp").forward(request, response);
            } else if (status.equals("canceled")) {
                List<OrderRepository.OrderSummary> canceledOrders = OrderRepository
                        .getCanceledOrdersBySeller(currentUser.getUserId());
                request.setAttribute("canceledOrders", canceledOrders);
                request.getRequestDispatcher("/user/canceled-orders.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/secured/user/pending-orders?status=pending");
            }
        } catch (Exception e) {
            e.printStackTrace();
            String error = URLEncoder.encode("Không thể tải danh sách đơn hàng.", "UTF-8");
            response.sendRedirect(request.getContextPath() + "/secured/user/pending-orders?status=pending&error=" + error);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        Integer orderId = Integer.valueOf(req.getParameter("orderId"));
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        try {
            if (action.equals("confirm")) {
                orderDAO.confirmOrderById(orderId);
                Behavior behavior = behaviorRepository.findByCode("CONFIRM_ORDER");
                behaviorRepository.insertLog(currentUser.getUserId(), behavior.getBehaviorId());

                String message = URLEncoder.encode("Xác nhận đơn hàng thành công!", "UTF-8");
                resp.sendRedirect(req.getContextPath() + "/secured/user/pending-orders?status=pending&success=" + message);

            } else if (action.equals("detail")) {
                req.getRequestDispatcher("/user/detail-order-seller.jsp").forward(req, resp);
            } else {
                String error = URLEncoder.encode("Hành động không hợp lệ.", "UTF-8");
                resp.sendRedirect(req.getContextPath() + "/secured/user/pending-orders?status=pending&error=" + error);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String error = URLEncoder.encode("Lỗi trong quá trình xử lý đơn hàng.", "UTF-8");
            resp.sendRedirect(req.getContextPath() + "/secured/user/pending-orders?status=pending&error=" + error);
        }
    }
}
