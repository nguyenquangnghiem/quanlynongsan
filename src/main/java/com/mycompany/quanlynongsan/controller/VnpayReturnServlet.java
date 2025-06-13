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

@WebServlet(urlPatterns = "/vnpay_return")
public class VnpayReturnServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    
    private CategoryDAO categoryDAO = new CategoryDAO();
    
    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode"); // Mã phản hồi từ VNPay
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");             // Mã đơn hàng (tham chiếu từ hệ thống của bạn)

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.getWriter().println("Bạn chưa đăng nhập!");
            return;
        }

        if ("00".equals(vnp_ResponseCode)) {
            try {
                // ✅ Tạo đối tượng Order mới
                Date now = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.DAY_OF_MONTH, 3); // Tăng 3 ngày

                Order order = new Order();
                order.setEstimatedTime(cal.getTime());   // Ngày giao hàng dự kiến
                order.setComment(null);                  // Có thể cho phép người dùng nhập comment sau
                order.setStatus("PAID");                 // Trạng thái đơn hàng
                order.setRate(null);                     // Chưa đánh giá
                order.setUserId(user.getUserId());       // ID người dùng hiện tại
                order.setCreatedDate(now);               // Ngày tạo đơn hàng
                order.setPaymentMethod("Thanh toán bằng VNpay");

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

        } else {
            response.getWriter().println("❌ Thanh toán thất bại!");
        }
    }
}
