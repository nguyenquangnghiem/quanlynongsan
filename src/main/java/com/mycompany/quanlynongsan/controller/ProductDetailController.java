/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.google.gson.Gson;
import com.mycompany.quanlynongsan.dao.ProductDAO;
import com.mycompany.quanlynongsan.dto.ProductDTO;
import com.mycompany.quanlynongsan.model.Product;
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
@WebServlet(urlPatterns = "/user/product-detail")
public class ProductDetailController extends HttpServlet {
    
    private ProductDAO productDAO = new ProductDAO();
    
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String productId = request.getParameter("productId");

    // Tìm product theo ID (ví dụ tạm)
      ProductDTO product = productDAO.findById(Integer.valueOf(productId));

    // Gửi về dạng JSON
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    Gson gson = new Gson();
    String json = gson.toJson(product);
    response.getWriter().write(json);
  }
}
