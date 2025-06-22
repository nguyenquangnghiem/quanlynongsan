/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.quanlynongsan.dao.OrderDAO;
import com.mycompany.quanlynongsan.dao.ProductDAO;
import com.mycompany.quanlynongsan.dao.UserDAO;
import com.mycompany.quanlynongsan.dto.ProductDTO;
import com.mycompany.quanlynongsan.model.Order;
import com.mycompany.quanlynongsan.response.ReviewResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author nghiem
 */

@WebServlet(urlPatterns = { "/product-detail" })
public class ProductDetailServlet extends HttpServlet {

    private ProductDAO productDAO = new ProductDAO();

    private OrderDAO orderDAO = new OrderDAO();

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("productId");

        // Tìm product theo ID (ví dụ tạm)
        ProductDTO product = productDAO.findById(Integer.valueOf(productId));
        List<Order> order = orderDAO.getOrderByProductId(Integer.valueOf(productId));
        List<ReviewResponse> reviews = new ArrayList<>();
        for (Order o : order) {
            if (o.getRate() != null) {
                reviews.add(
                        new ReviewResponse(o.getComment(), o.getRate(), userDAO.findById(o.getUserId()).getFullName()));
            }
        }
        req.setAttribute("product", product);
        req.setAttribute("reviews", reviews);
        req.getRequestDispatcher("/product-detail.jsp").forward(req, resp);
    }
}
