/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.util.List;

import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.ProductRepository;

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
@WebServlet(urlPatterns = "/secured/user/my-stock")
public class MyStockServlet extends HttpServlet {

    private ProductRepository productRepository = new ProductRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String success = req.getParameter("success");
    String error = req.getParameter("error");

    if (success != null) {
        success = java.net.URLDecoder.decode(success, "UTF-8");
        req.setAttribute("success", success);
    }
    
    if (error != null) {
        error = java.net.URLDecoder.decode(error, "UTF-8");
        req.setAttribute("error", error);
    }
        List<Product> products = productRepository.findByHolderId(user.getUserId());
        req.setAttribute("products", products);
        req.getRequestDispatcher("/user/my-stock.jsp").forward(req, resp);
    }

}
