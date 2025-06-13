/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.quanlynongsan.dao.ProductDAO;
import com.mycompany.quanlynongsan.dto.ProductDTO;
import com.mycompany.quanlynongsan.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author nghiem
 */

@WebServlet(urlPatterns = {"/user/category"})
public class Category extends HttpServlet {
    
    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Integer categoryId = Integer.valueOf(req.getParameter("categoryId"));
    List<ProductDTO> products;
    if(categoryId != 0){
        products = productDAO.find10(Integer.valueOf(categoryId));
    } else {
        products = productDAO.findAll();
    }

    resp.setContentType("application/json");
    new ObjectMapper().writeValue(resp.getWriter(), products);
}

    
}
