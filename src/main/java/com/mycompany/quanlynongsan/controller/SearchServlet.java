/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.dao.CategoryDAO;
import com.mycompany.quanlynongsan.dao.ProductDAO;
import com.mycompany.quanlynongsan.dto.ProductDTO;
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

@WebServlet(urlPatterns = {"/user/search"})
public class SearchServlet extends HttpServlet{
    
     private CategoryDAO categoryDAO = new CategoryDAO();
     
     private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<com.mycompany.quanlynongsan.model.Category> categories = new ArrayList<>();
        categories.add(new com.mycompany.quanlynongsan.model.Category(0, "Tất cả"));
        categories.addAll(categoryDAO.findAll());
        List<ProductDTO> products = productDAO.findAll();
         req.setAttribute("products", products);

        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/user/search.jsp").forward(req, resp);
    }
    
    @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter("name");

    // Nhận nhiều origin (checkbox hoặc multiple select sẽ truyền nhiều origin)
    String[] origins = req.getParameterValues("origin");

    String minPrice = req.getParameter("minPrice");
    String maxPrice = req.getParameter("maxPrice");
    String categoryIdStr = req.getParameter("category");

    Double minPriceNum = null;
    Double maxPriceNum = null;
    if (minPrice != null && !minPrice.isEmpty()) {
        try {
            minPriceNum = Double.parseDouble(minPrice);
        } catch (NumberFormatException ignored) {}
    }

    if (maxPrice != null && !maxPrice.isEmpty()) {
        try {
            maxPriceNum = Double.parseDouble(maxPrice);
        } catch (NumberFormatException ignored) {}
    }

    Integer categoryId = null;
    if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
        try {
            categoryId = Integer.parseInt(categoryIdStr);
        } catch (NumberFormatException ignored) {}
    }

    // Gọi DAO để tìm kiếm - truyền list<String> origin
    ProductDAO productDAO = new ProductDAO();
    List<ProductDTO> productList = productDAO.searchProducts(name, origins, minPriceNum, maxPriceNum, categoryId);

    // Đưa kết quả vào request để hiển thị trên JSP
    req.setAttribute("products", productList);

    List<com.mycompany.quanlynongsan.model.Category> categories = new ArrayList<>();
    categories.add(new com.mycompany.quanlynongsan.model.Category(0, "Tất cả"));
    categories.addAll(categoryDAO.findAll());
    req.setAttribute("categories", categories);

    req.getRequestDispatcher("/user/search.jsp").forward(req, resp);
}

    
}
