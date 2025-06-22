package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;

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

@WebServlet(urlPatterns = "/confirm-stock")
public class ConfirmStockServlet extends HttpServlet {

    private final OrderRepository orderRepository = new OrderRepository();
    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer orderId = Integer.valueOf(req.getParameter("orderId"));
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String redirectURL = req.getContextPath() + "/secured/user/orders-not-imported";

        try {
            orderRepository.importProductsForUser(orderId, user.getUserId());

            Behavior behavior = behaviorRepository.findByCode("IMPORT_PRODUCT");
            behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());

            String successMsg = URLEncoder.encode("Xác nhận nhập kho thành công!", "UTF-8");
            resp.sendRedirect(redirectURL + "?success=" + successMsg);

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = URLEncoder.encode("❌ Đã xảy ra lỗi khi xác nhận nhập kho!", "UTF-8");
            resp.sendRedirect(redirectURL + "?error=" + errorMsg);
        }
    }
}
