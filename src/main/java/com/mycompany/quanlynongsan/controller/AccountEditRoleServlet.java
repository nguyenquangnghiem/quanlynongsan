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

@WebServlet(urlPatterns = "/secured/admin/account/edit-role")
public class AccountEditRoleServlet extends HttpServlet {

    private UserRepository userRepository = new UserRepository();

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = Integer.parseInt(req.getParameter("userId"));
        User user = userRepository.findById(userId);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/admin/account-edit-role.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = Integer.parseInt(req.getParameter("userId"));
        Integer roleId = Integer.parseInt(req.getParameter("roleId"));
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String redirectURL = req.getContextPath() + "/secured/admin/account";
        try (var conn = com.mycompany.quanlynongsan.config.DatabaseConnection.getConnection(); var stmt = conn.prepareStatement("UPDATE [USER] SET role_id = ? WHERE user_id = ?")) {

            stmt.setInt(1, roleId);
            stmt.setInt(2, userId);
            int affected = stmt.executeUpdate();

            if (affected > 0) {
                Behavior behavior = behaviorRepository.findByCode("EDIT_USER_ROLE");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());

                String msg = java.net.URLEncoder.encode("Thay đổi vai trò thành công!", "UTF-8");
                resp.sendRedirect(redirectURL + "?success=" + msg);
            } else {
                String msg = java.net.URLEncoder.encode("Không tìm thấy người dùng để thay đổi vai trò!", "UTF-8");
                resp.sendRedirect(redirectURL + "?error=" + msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String msg = java.net.URLEncoder.encode("Có lỗi xảy ra khi thay đổi vai trò!", "UTF-8");
            resp.sendRedirect(redirectURL + "?error=" + msg);
        }
    }

}
