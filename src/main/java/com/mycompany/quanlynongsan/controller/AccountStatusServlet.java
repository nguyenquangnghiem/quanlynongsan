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

        if (user != null) {
            try (Connection conn = com.mycompany.quanlynongsan.config.DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE [USER] SET is_active = ? WHERE user_id = ?")) {
                stmt.setBoolean(1, !user.getIsActive()); // Đảo ngược trạng thái
                stmt.setInt(2, userId);
                stmt.executeUpdate();
                if(user.getIsActive()) {
                    Behavior behavior = behaviorRepository.findByCode("LOCK_USER");
                    behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                } else {
                    Behavior behavior = behaviorRepository.findByCode("UNLOCK_USER");
                    behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resp.sendRedirect(req.getContextPath() + "/secured/admin/account");
    }
}
