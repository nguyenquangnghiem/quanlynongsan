package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet(urlPatterns = "/secured/admin/account/status")
public class AccountStatusServlet extends HttpServlet {

    private UserRepository userRepository = new UserRepository();
    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = Integer.parseInt(req.getParameter("userId"));
        User user = userRepository.findByIdAndNonIsActive(userId);
        HttpSession session = req.getSession();
        User curUser = (User) session.getAttribute("user");

        String redirectURL = req.getContextPath() + "/secured/admin/account";
        String success = null;
        String error = null;

        if (user != null) {
            try (Connection conn = com.mycompany.quanlynongsan.config.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE [USER] SET is_active = ? WHERE user_id = ?")) {

                stmt.setBoolean(1, !user.getIsActive()); // Đảo ngược trạng thái
                stmt.setInt(2, userId);
                stmt.executeUpdate();

                Behavior behavior;
                if (user.getIsActive()) {
                    behavior = behaviorRepository.findByCode("LOCK_USER");
                    success = "Khóa tài khoản thành công!";
                } else {
                    behavior = behaviorRepository.findByCode("UNLOCK_USER");
                    success = "Mở khóa tài khoản thành công!";
                }
                behaviorRepository.insertLog(curUser.getUserId(), behavior.getBehaviorId());

            } catch (Exception e) {
                e.printStackTrace();
                error = "Có lỗi xảy ra khi thay đổi trạng thái tài khoản!";
            }
        } else {
            error = "Không tìm thấy tài khoản!";
        }

        if (success != null) {
            redirectURL += "?success=" + URLEncoder.encode(success, "UTF-8");
        } else if (error != null) {
            redirectURL += "?error=" + URLEncoder.encode(error, "UTF-8");
        }

        resp.sendRedirect(redirectURL);
    }
}
