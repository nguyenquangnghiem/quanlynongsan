package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.repository.ProductRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/secured/admin/product-pending")
public class ProductPendingServlet extends HttpServlet {

    private ProductRepository productRepository = new ProductRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> pendingProducts = productRepository.findByStatus(false);
        req.setAttribute("products", pendingProducts);
        req.getRequestDispatcher("/admin/product-pending.jsp").forward(req, resp);
    }
}
