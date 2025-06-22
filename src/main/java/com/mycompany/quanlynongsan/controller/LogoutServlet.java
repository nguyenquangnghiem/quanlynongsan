package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;

import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false); // Không tạo mới nếu chưa có
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (session != null) {
            session.invalidate(); // Hủy session hiện tại
        }

        if (user != null) {
            try {
                Behavior behavior = behaviorRepository.findByCode("LOGOUT");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
            } catch (Exception e) {
                e.printStackTrace(); // Có thể log thêm nếu muốn
            }
        }

        String message = URLEncoder.encode("Đăng xuất thành công!", "UTF-8");
        resp.sendRedirect(req.getContextPath() + "/login.jsp?success=" + message);
    }
}
