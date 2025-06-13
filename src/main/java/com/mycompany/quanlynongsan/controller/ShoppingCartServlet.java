/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.dao.HasCartDAO;
import com.mycompany.quanlynongsan.dto.CartSummary;
import com.mycompany.quanlynongsan.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author nghiem
 */

@WebServlet(urlPatterns = "/secured/user/shopping-cart")
public class ShoppingCartServlet extends HttpServlet {
    
    private HasCartDAO hasCartDAO = new HasCartDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        CartSummary summary = hasCartDAO.getCartSummary(user.getUserId());
        
        req.setAttribute("summary", summary);
        
        req.getRequestDispatcher("/user/shopping-cart.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    User user = (User) session.getAttribute("user");

    String action = req.getParameter("action");

    if ("delete".equals(action)) {
        String productIdParam = req.getParameter("productId");

        if (productIdParam != null) {
            try {
                int productId = Integer.parseInt(productIdParam);
                hasCartDAO.removeProductFromCart(user.getUserId(), productId);

                CartSummary summary = hasCartDAO.getCartSummary(user.getUserId());
        
                req.setAttribute("summary", summary);

                req.getRequestDispatcher("/user/shopping-cart.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid productId");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing productId");
        }
    } else {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
    }
}

}
