package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;

import com.mycompany.quanlynongsan.dao.HasCartDAO;
import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.response.CartSummaryResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/secured/user/shopping-cart")
public class ShoppingCartServlet extends HttpServlet {

    private final HasCartDAO hasCartDAO = new HasCartDAO();
    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        CartSummaryResponse summary = hasCartDAO.getCartSummary(user.getUserId());
        req.setAttribute("summary", summary);

        Behavior behavior = behaviorRepository.findByCode("VIEW_CART");
        behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
        String success = req.getParameter("success");
    String error = req.getParameter("error");

    if (success != null) {
        success = java.net.URLDecoder.decode(success, "UTF-8");
        req.setAttribute("success", success);
    }
    
    if (error != null) {
        error = java.net.URLDecoder.decode(error, "UTF-8");
        req.setAttribute("error", error);
    }

        req.getRequestDispatcher("/user/shopping-cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            String productIdParam = req.getParameter("productId");

            if (productIdParam != null) {
                try {
                    int productId = Integer.parseInt(productIdParam);
                    hasCartDAO.removeProductFromCart(user.getUserId(), productId);

                    Behavior behavior = behaviorRepository.findByCode("REMOVE_FROM_CART");
                    behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());

                    String successMsg = "Xóa sản phẩm khỏi giỏ hàng thành công.";
                    resp.sendRedirect(req.getContextPath() + "/secured/user/shopping-cart?success=" + URLEncoder.encode(successMsg, "UTF-8"));

                } catch (NumberFormatException e) {
                    String errorMsg = "ID sản phẩm không hợp lệ.";
                    resp.sendRedirect(req.getContextPath() + "/secured/user/shopping-cart?error=" + URLEncoder.encode(errorMsg, "UTF-8"));
                }
            } else {
                String errorMsg = "Thiếu ID sản phẩm.";
                resp.sendRedirect(req.getContextPath() + "/secured/user/shopping-cart?error=" + URLEncoder.encode(errorMsg, "UTF-8"));
            }
        } else {
            String errorMsg = "Thao tác không hợp lệ.";
            resp.sendRedirect(req.getContextPath() + "/secured/user/shopping-cart?error=" + URLEncoder.encode(errorMsg, "UTF-8"));
        }
    }
}
