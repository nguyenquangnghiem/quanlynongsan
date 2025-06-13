/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.HasCartRepository;
import com.mycompany.quanlynongsan.repository.HasLikeProductRepository;
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

@WebServlet(urlPatterns = "/secured/user/favorite-products")
public class FavProductServlet extends HttpServlet {
    
    private HasCartRepository hasCartRepository = new HasCartRepository();
    private HasLikeProductRepository hasLikeProductRepository = new HasLikeProductRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/favorite-products.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer productId = Integer.valueOf(req.getParameter("productId"));
        String action = req.getParameter("action");
        User user = (User) session.getAttribute("user");
        if(action.equals("addToCart")){
            hasCartRepository.addToCart(user.getUserId(), productId, 1);
        } else if (action.equals("delete")){
            hasLikeProductRepository.removeFavoriteProduct(user.getUserId(), productId);
        }
        req.getRequestDispatcher("/user/favorite-products.jsp").forward(req, resp);
    }
}
