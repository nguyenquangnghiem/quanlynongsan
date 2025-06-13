/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.dao.CategoryDAO;
import com.mycompany.quanlynongsan.dao.ProductDAO;
import com.mycompany.quanlynongsan.dto.ProductDTO;
import com.mycompany.quanlynongsan.model.Category;
import com.mycompany.quanlynongsan.model.Product;
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

@WebServlet(urlPatterns = {"/user/home"})
public class HomeUserController extends HttpServlet {
    
    private CategoryDAO categoryDAO = new CategoryDAO();
    
    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          List<Category> categories = new ArrayList<>();
        categories.add(new Category(0, "Tất cả"));
        categories.addAll(categoryDAO.findAll());

        req.setAttribute("categories", categories);
         List<ProductDTO> products = productDAO.findAll();
         req.setAttribute("products", products);
        req.getRequestDispatcher("/user/home.jsp").forward(req, resp);
    }
    
}
