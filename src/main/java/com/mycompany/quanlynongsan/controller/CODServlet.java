/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.dao.CategoryDAO;
import com.mycompany.quanlynongsan.dao.OrderDAO;
import com.mycompany.quanlynongsan.dao.ProductDAO;
import com.mycompany.quanlynongsan.dto.ProductDTO;
import com.mycompany.quanlynongsan.model.Order;
import com.mycompany.quanlynongsan.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nghiem
 */

@WebServlet(urlPatterns = "/secured/cod")
public class CODServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    
    private CategoryDAO categoryDAO = new CategoryDAO();
    
    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.getWriter().println("Bạn chưa đăng nhập!");
            return;
        }

            try {
                // ✅ Tạo đối tượng Order mới
                Date now = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.DAY_OF_MONTH, 3); // Tăng 3 ngày

                Order order = new Order();
                order.setEstimatedTime(cal.getTime());   // Ngày giao hàng dự kiến
                order.setComment(null);                  // Có thể cho phép người dùng nhập comment sau
                order.setStatus("PENDING");                 // Trạng thái đơn hàng
                order.setRate(null);                     // Chưa đánh giá
                order.setUserId(user.getUserId());       // ID người dùng hiện tại
                order.setCreatedDate(now);               // Ngày tạo đơn hàng
                order.setPaymentMethod("Thanh toán khi nhận hàng (COD)");

                // ✅ Lưu đơn hàng mới, lấy về orderId
                orderDAO.saveOrder(order);

                // ✅ Chuyển các sản phẩm từ giỏ hàng (HAS_CART) vào bảng ORDER_PRODUCT
                orderDAO.transferCartToOrder(user.getUserId(), order.getOrderId());
                List<com.mycompany.quanlynongsan.model.Category> categories = new ArrayList<>();
        categories.add(new com.mycompany.quanlynongsan.model.Category(0, "Tất cả"));
        categories.addAll(categoryDAO.findAll());

        request.setAttribute("categories", categories);
         List<ProductDTO> products = productDAO.findAll();
         request.setAttribute("products", products);

                request.getRequestDispatcher("/user/home.jsp").forward(request, response);

            } catch (Exception ex) {
                ex.printStackTrace();
                response.getWriter().println("❌ Đã xảy ra lỗi khi xử lý đơn hàng.");
            }
    }
}
