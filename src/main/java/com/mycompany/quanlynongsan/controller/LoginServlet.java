/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.quanlynongsan.dao.CategoryDAO;
import com.mycompany.quanlynongsan.dao.ProductDAO;
import com.mycompany.quanlynongsan.dao.UserDAO;
import com.mycompany.quanlynongsan.dto.ProductDTO;
import com.mycompany.quanlynongsan.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author nghiem
 */
@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    final private UserDAO userDAO = new UserDAO();

    private CategoryDAO categoryDAO = new CategoryDAO();

    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = String.valueOf(req.getParameter("email"));
        String password = String.valueOf(req.getParameter("password"));

        if (userDAO.login(email, password, req)) {
            List<com.mycompany.quanlynongsan.model.Category> categories = new ArrayList<>();
            categories.add(new com.mycompany.quanlynongsan.model.Category(0, "Tất cả"));
            categories.addAll(categoryDAO.findAll());

            req.setAttribute("categories", categories);
            List<ProductDTO> products = productDAO.findAll(2);
            req.setAttribute("products", products);
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            if (user.getRoleId() == 4) {
                String message = "Đăng nhập thành công!";
                String encodedMsg = java.net.URLEncoder.encode(message, "UTF-8");
                resp.sendRedirect(req.getContextPath() + "/secured/admin/account?success=" + encodedMsg);
            } else {
                req.setAttribute("success", "Đăng nhập thành công!");
                req.getRequestDispatcher("user/home.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

}
