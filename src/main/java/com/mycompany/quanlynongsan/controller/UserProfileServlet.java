package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;

import org.mindrot.jbcrypt.BCrypt;

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

@WebServlet(urlPatterns = "/secured/user/profile")
public class UserProfileServlet extends HttpServlet {

    private final UserRepository userRepository = new UserRepository();
    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/user-profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String success = null;
        String error = null;

        try {
            if (action.equals("updateInformation")) {
                String email = req.getParameter("email");
                String phoneNumber = req.getParameter("phoneNumber");
                String fullName = req.getParameter("fullName");
                user.setFullName(fullName);
                user.setPhoneNumber(phoneNumber);
                user.setEmail(email);
                userRepository.save(user);
                Behavior behavior = behaviorRepository.findByCode("UPDATE_PROFILE");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                success = "Cập nhật thông tin thành công!";
            } else if (action.equals("updateAddress")) {
                String address = req.getParameter("address");
                String ward = req.getParameter("ward");
                String district = req.getParameter("district");
                String province = req.getParameter("province");
                user.setAddress(address + ", " + ward + ", " + district + ", " + province);
                userRepository.save(user);
                Behavior behavior = behaviorRepository.findByCode("UPDATE_PROFILE");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                success = "Cập nhật địa chỉ thành công!";
            } else if (action.equals("updatePassword")) {
                String nowPass = req.getParameter("nowPass");
                String newPass = req.getParameter("newPass");
                String confirmNewPass = req.getParameter("confirmNewPass");
                if (BCrypt.checkpw(nowPass, user.getPassword())) {
                    if (newPass.equals(confirmNewPass)) {
                        user.setPassword(BCrypt.hashpw(newPass, BCrypt.gensalt()));
                        userRepository.save(user);
                        Behavior behavior = behaviorRepository.findByCode("RESET_PASSWORD");
                        behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                        success = "Thay đổi mật khẩu thành công!";
                    } else {
                        error = "Mật khẩu xác nhận không khớp!";
                    }
                } else {
                    error = "Mật khẩu hiện tại không chính xác!";
                }
            }
            session.setAttribute("user", user);

        } catch (Exception e) {
            e.printStackTrace();
            error = "Đã xảy ra lỗi trong quá trình cập nhật!";
        }

        if (success != null) {
            resp.sendRedirect(req.getContextPath() + "/secured/user/profile?success=" + URLEncoder.encode(success, "UTF-8"));
        } else {
            resp.sendRedirect(req.getContextPath() + "/secured/user/profile?error=" + URLEncoder.encode(error, "UTF-8"));
        }
    }
}
