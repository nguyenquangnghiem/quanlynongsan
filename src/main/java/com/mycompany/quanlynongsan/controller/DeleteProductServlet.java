package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.ProductRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/secured/user/delete-product")
public class DeleteProductServlet extends HttpServlet {

    private final ProductRepository productRepository = new ProductRepository();
    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectURL = req.getContextPath() + "/secured/user/my-products";

        try {
            Integer idProduct = Integer.valueOf(req.getParameter("id"));

            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            productRepository.deleteProduct(idProduct);

            Behavior behavior = behaviorRepository.findByCode("DELETE_PRODUCT");
            behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());

            String successMsg = URLEncoder.encode("Xóa sản phẩm thành công!", "UTF-8");
            resp.sendRedirect(redirectURL + "?success=" + successMsg);

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = URLEncoder.encode("❌ Xóa sản phẩm thất bại!", "UTF-8");
            resp.sendRedirect(redirectURL + "?error=" + errorMsg);
        }
    }
}
