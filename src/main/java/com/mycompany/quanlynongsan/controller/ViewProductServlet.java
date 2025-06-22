/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.quanlynongsan.model.ImageProduct;
import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.repository.CategoryRepository;
import com.mycompany.quanlynongsan.repository.ImageProductRepository;
import com.mycompany.quanlynongsan.repository.ProductRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author nghiem
 */
@WebServlet(urlPatterns = "/secured/user/view-product")
public class ViewProductServlet extends HttpServlet {

    private ProductRepository productRepository = new ProductRepository();

    private CategoryRepository categoryRepository = new CategoryRepository();

    private ImageProductRepository imageProductRepository = new ImageProductRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int productId = Integer.parseInt(req.getParameter("id"));
        Product product = productRepository.findByIdNonIsBrowseNonIsSell(productId); // lấy sản phẩm
        List<com.mycompany.quanlynongsan.model.Category> allCategories = categoryRepository.findAll(); // lấy toàn bộ
                                                                                                       // danh mục
        List<com.mycompany.quanlynongsan.model.Category> selectedCategoryIds = categoryRepository
                .findCategoriesByProductId(productId); // lấy danh mục đã chọn
        List<ImageProduct> imageProducts = imageProductRepository.findByProductId(productId);

        req.setAttribute("product", product);
        req.setAttribute("allCategories", allCategories);
        req.setAttribute("selectedCategoryIds", selectedCategoryIds);
        List<String> imageUrls = new ArrayList<>();
        for (ImageProduct imageProduct : imageProducts) {
            imageUrls.add(imageProduct.getUrlImage());
        }
        req.setAttribute("imageUrls", imageUrls);
        req.getRequestDispatcher("/user/view-product.jsp").forward(req, resp);
    }

}
