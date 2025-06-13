/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.ProductRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author nghiem
 */
@WebServlet(urlPatterns = "/secured/user/delete-product")
public class DeleteProductServlet extends HttpServlet{
    
    private ProductRepository productRepository = new ProductRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer idProduct = Integer.valueOf(req.getParameter("id"));
        productRepository.deleteProduct(idProduct);
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        List<Product> products = productRepository.findByHolderId(user.getUserId());
        req.setAttribute("products", products);
        req.getRequestDispatcher("/user/my-product.jsp").forward(req, resp);
    }
    
}
