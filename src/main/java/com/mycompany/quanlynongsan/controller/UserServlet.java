/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author nghiem
 */

@WebServlet(urlPatterns = "/secured/user/profile")
public class UserServlet extends HttpServlet {
    
    private UserRepository userRepository = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/user-profile.jsp").forward(req, resp);
    }
    
    

   @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String message = null;
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
                message = "Cập nhật thông tin thành công!";
            } else if (action.equals("updateAddress")) {
                String address = req.getParameter("address");
                String ward = req.getParameter("ward");
                String district = req.getParameter("district");
                String province = req.getParameter("province");
                user.setAddress(address + ", " + ward + ", " + district + ", " + province);
                userRepository.save(user);
                message = "Cập nhật địa chỉ thành công!";
            } else if (action.equals("updatePassword")) {
                String nowPass = req.getParameter("nowPass");
                String newPass = req.getParameter("newPass");
                String confirmNewPass = req.getParameter("confirmNewPass");
                if (BCrypt.checkpw(nowPass, user.getPassword())) {
                    if (newPass.equals(confirmNewPass)) {
                        user.setPassword(BCrypt.hashpw(newPass, BCrypt.gensalt()));
                        userRepository.save(user);
                        message = "Thay đổi mật khẩu thành công!";
                    } else {
                        error = "Mật khẩu xác nhận không khớp!";
                    }
                } else {
                    error = "Mật khẩu hiện tại không chính xác!";
                }
            }
            session.setAttribute("user", user);
        } catch (Exception e) {
            error = "Đã xảy ra lỗi trong quá trình cập nhật!";
        }

        req.setAttribute("message", message);
        req.setAttribute("error", error);
        req.getRequestDispatcher("/user/user-profile.jsp").forward(req, resp);
    }
    
}
