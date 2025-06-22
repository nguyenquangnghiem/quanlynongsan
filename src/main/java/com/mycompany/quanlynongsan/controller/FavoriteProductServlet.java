package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;

import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.HasCartRepository;
import com.mycompany.quanlynongsan.repository.HasLikeProductRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/secured/user/favorite-products")
public class FavoriteProductServlet extends HttpServlet {

    private final HasCartRepository hasCartRepository = new HasCartRepository();
    private final HasLikeProductRepository hasLikeProductRepository = new HasLikeProductRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/favorite-products.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        Integer productId = Integer.valueOf(req.getParameter("productId"));
        String action = req.getParameter("action");

        try {
            String successMsg = "";
            if ("addToCart".equals(action)) {
                hasCartRepository.addToCart(user.getUserId(), productId, 1);
                successMsg = "Thêm vào giỏ hàng thành công!";
            } else if ("delete".equals(action)) {
                hasLikeProductRepository.removeFavoriteProduct(user.getUserId(), productId);
                successMsg = "Đã xóa khỏi danh sách yêu thích!";
            }
            resp.sendRedirect(req.getContextPath() + "/secured/user/favorite-products?success=" + URLEncoder.encode(successMsg, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = URLEncoder.encode("❌ Thao tác thất bại!", "UTF-8");
            resp.sendRedirect(req.getContextPath() + "/secured/user/favorite-products?error=" + errorMsg);
        }
    }
}
