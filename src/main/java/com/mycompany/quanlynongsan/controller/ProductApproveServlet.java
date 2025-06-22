package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.ProductRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(urlPatterns = "/secured/admin/product-approve")
public class ProductApproveServlet extends HttpServlet {

    private ProductRepository productRepository = new ProductRepository();
    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer productId = Integer.parseInt(req.getParameter("productId"));
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false); // Không tạo mới nếu chưa có
        User user = (User) session.getAttribute("user");

        String successMessage = "";
        String errorMessage = "";

        try {
            if ("approve".equals(action)) {
                productRepository.updateStatus(productId, true);
                Behavior behavior = behaviorRepository.findByCode("APPROVE_PRODUCT");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                successMessage = "Phê duyệt sản phẩm thành công!";
            } else if ("reject".equals(action)) {
                productRepository.updateActive(productId, false);
                Behavior behavior = behaviorRepository.findByCode("REJECT_PRODUCT");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                successMessage = "Từ chối sản phẩm thành công!";
            } else {
                errorMessage = "Hành động không hợp lệ!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Đã xảy ra lỗi trong quá trình xử lý.";
        }

        String redirectUrl = req.getContextPath() + "/secured/admin/product-pending";

        if (!successMessage.isEmpty()) {
            redirectUrl += "?success=" + URLEncoder.encode(successMessage, "UTF-8");
        } else if (!errorMessage.isEmpty()) {
            redirectUrl += "?error=" + URLEncoder.encode(errorMessage, "UTF-8");
        }

        resp.sendRedirect(redirectUrl);
    }
}
