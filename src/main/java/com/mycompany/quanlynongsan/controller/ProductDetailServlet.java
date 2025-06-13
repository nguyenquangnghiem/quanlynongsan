/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.dao.OrderDAO;
import com.mycompany.quanlynongsan.dao.ProductDAO;
import com.mycompany.quanlynongsan.dao.UserDAO;
import com.mycompany.quanlynongsan.dto.ProductDTO;
import com.mycompany.quanlynongsan.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nghiem
 */

@WebServlet(urlPatterns = {"/product-detail"})
public class ProductDetailServlet extends HttpServlet{
    
    private ProductDAO productDAO = new ProductDAO();
    
    private OrderDAO orderDAO = new OrderDAO();
    
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("productId");

        // Tìm product theo ID (ví dụ tạm)
        ProductDTO product = productDAO.findById(Integer.valueOf(productId));
        List<Order> order = orderDAO.getOrderByProductId(Integer.valueOf(productId));
        List<Reviews> reviews = new ArrayList<>();
        for(Order o : order) {
            if(o.getRate() != null){
                reviews.add(new Reviews(o.getComment(), o.getRate(), userDAO.findById(o.getUserId()).getFullName()));
            }
        }
        req.setAttribute("product", product);
        req.setAttribute("reviews", reviews);
        req.getRequestDispatcher("/product-detail.jsp").forward(req, resp);
    }
    
    public class Reviews {
        private String comment;
        private Integer rating;
        private String username;

        public Reviews(String comment, Integer rating, String username) {
            this.comment = comment;
            this.rating = rating;
            this.username = username;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
