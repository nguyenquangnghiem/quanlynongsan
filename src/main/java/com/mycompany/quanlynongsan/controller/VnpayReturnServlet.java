package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.dao.CategoryDAO;
import com.mycompany.quanlynongsan.dao.OrderDAO;
import com.mycompany.quanlynongsan.dao.ProductDAO;
import com.mycompany.quanlynongsan.dto.ProductDTO;
import com.mycompany.quanlynongsan.model.Behavior;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = "/vnpay_return")
public class VnpayReturnServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode"); // Mã phản hồi từ VNPay
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=" + URLEncoder.encode("Vui lòng đăng nhập để tiếp tục.", "UTF-8"));
            return;
        }

        try {
            if ("00".equals(vnp_ResponseCode)) {
                // ✅ Tạo đối tượng Order mới
                Date now = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.DAY_OF_MONTH, 3); // Ngày giao dự kiến

                Order order = new Order();
                order.setEstimatedTime(cal.getTime());
                order.setComment(null);
                order.setStatus("PAID");
                order.setRate(null);
                order.setUserId(user.getUserId());
                order.setCreatedDate(now);
                order.setPaymentMethod("Thanh toán bằng VNPay");
                order.setIsImported(false);

                // ✅ Thêm đơn hàng
                orderDAO.transferCartToOrder(user.getUserId(), order);

                // ✅ Lấy dữ liệu trang home
                List<com.mycompany.quanlynongsan.model.Category> categories = new ArrayList<>();
                categories.add(new com.mycompany.quanlynongsan.model.Category(0, "Tất cả"));
                categories.addAll(categoryDAO.findAll());
                Integer roleId = (user.getRoleId() != 1) ? user.getRoleId() - 1 : 1;
                List<ProductDTO> products = productDAO.findAll(roleId);

                request.setAttribute("categories", categories);
                request.setAttribute("products", products);
                request.setAttribute("success", "Thanh toán thành công! Đơn hàng đã được tạo.");

                Behavior behavior = behaviorRepository.findByCode("PAYMENT_SUCCESSFUL");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());

                request.getRequestDispatcher("/user/home.jsp").forward(request, response);

            } else {
                String error = "Thanh toán thất bại! Mã lỗi: " + vnp_ResponseCode;
                response.sendRedirect(request.getContextPath() + "/user/home.jsp?error=" + URLEncoder.encode(error, "UTF-8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String error = "❌ Đã xảy ra lỗi khi xử lý đơn hàng.";
            response.sendRedirect(request.getContextPath() + "/user/home.jsp?error=" + URLEncoder.encode(error, "UTF-8"));
        }
    }
}
