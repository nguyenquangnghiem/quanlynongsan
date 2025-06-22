
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

@WebServlet(urlPatterns = "/secured/admin/product-approve")
public class ProductApproveServlet extends HttpServlet {

    private ProductRepository productRepository = new ProductRepository();
    
    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer productId = Integer.parseInt(req.getParameter("productId"));
        String action = req.getParameter("action");
        HttpSession session = req.getSession(); // Không tạo mới nếu chưa có
        User user = (User) session.getAttribute("user");
        if ("approve".equals(action)) {
            productRepository.updateStatus(productId, true);  // Status mới là APPROVED (hoặc SUCCESSFUL nếu bạn muốn)
            Behavior behavior = behaviorRepository.findByCode("APPROVE_PRODUCT");
            behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
        } else if ("reject".equals(action)) {
            productRepository.updateActive(productId, false);
            Behavior behavior = behaviorRepository.findByCode("REJECT_PRODUCT");
            behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
        }
        resp.sendRedirect(req.getContextPath() + "/secured/admin/product-pending");
    }
}
