/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.dao.UserDAO;
import com.mycompany.quanlynongsan.request.RegisterRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author nghiem
 */

@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {
    
    final private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = String.valueOf(req.getParameter("email"));
        String password = String.valueOf(req.getParameter("password"));
        String confirmPassword = String.valueOf(req.getParameter("confirm-password"));
        String role = req.getParameter("role");
        Integer roleId = 3;
        if(role.equals("CUSTOMER")){
            roleId = 3;
        } else if (role.equals("FARMER")) {
            roleId = 1;
        }
        RegisterRequest request = new RegisterRequest(email, password, roleId);
        if(userDAO.register(request) && password.equals(confirmPassword)) {
            resp.sendRedirect("login.jsp");
        } else {
            resp.sendError(500);
        }
    }
    
}
