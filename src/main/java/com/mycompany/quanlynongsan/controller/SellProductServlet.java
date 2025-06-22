/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mycompany.quanlynongsan.config.CloudinaryConfig;
import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.ProductRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.net.URLEncoder;

/**
 *
 * @author nghiem
 */
@MultipartConfig
@WebServlet(urlPatterns = "/secured/user/sell-product")
public class SellProductServlet extends HttpServlet {

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/sell-product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String action = req.getParameter("action");
        if (action != null && action.equals("create")) {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("text/html;charset=UTF-8");

            try {
                // 1. Lấy thông tin sản phẩm từ request
                String name = req.getParameter("name");
                String description = req.getParameter("description");
                BigDecimal price = new BigDecimal(req.getParameter("price"));
                Integer quantity = Integer.parseInt(req.getParameter("quantity"));
                String status = req.getParameter("status");
                Boolean isSell = Boolean.parseBoolean(req.getParameter("is_sell"));
                Boolean isBrowse = Boolean.parseBoolean(req.getParameter("is_browse"));
                String placeOfManufacture = req.getParameter("place_of_manufacture");
                String[] categoryIdsParam = req.getParameterValues("category_ids");
                Integer[] categories = null;

                if (categoryIdsParam != null) {
                    categories = new Integer[categoryIdsParam.length];
                    for (int i = 0; i < categoryIdsParam.length; i++) {
                        categories[i] = Integer.parseInt(categoryIdsParam[i]);
                    }
                }
                Boolean isActive = Boolean.parseBoolean(req.getParameter("is_active"));
                Integer holderId = user.getUserId();
                Date createdDate = new Date();

                Product product = new Product(null, name, description, price, quantity, status, isSell, isBrowse,
                        placeOfManufacture, isActive, holderId, createdDate);

                // 2. Xử lý upload ảnh
                Collection<Part> fileParts = req.getParts();
                List<String> imageUrls = new ArrayList<>();
                Cloudinary cloudinary = CloudinaryConfig.getInstance();

                for (Part filePart : fileParts) {
                    String fileName = filePart.getSubmittedFileName();
                    if (fileName != null && !fileName.isEmpty()) {
                        File tempFile = File.createTempFile("upload-", fileName);
                        try (InputStream fileContent = filePart.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = fileContent.read(buffer)) != -1) {
                                out.write(buffer, 0, bytesRead);
                            }
                        }

                        try {
                            Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
                            String publicUrl = (String) uploadResult.get("url");
                            imageUrls.add(publicUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                            String errorMessage = "Upload thất bại: " + e.getMessage();
                            resp.sendRedirect(req.getContextPath() + "/secured/user/sell-product?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
                            return;
                        } finally {
                            tempFile.delete();
                        }
                    }
                }

                // 3. Lưu vào database
                ProductRepository repo = new ProductRepository();
                boolean success = repo.add(product, imageUrls, categories);

                if (success) {
                    Behavior behavior = behaviorRepository.findByCode("CREATE_PRODUCT");
                    behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                    resp.sendRedirect(req.getContextPath() + "/secured/user/my-products?success=" + URLEncoder.encode("Thêm sản phẩm mới thành công!", "UTF-8"));
                } else {
                    resp.sendRedirect(req.getContextPath() + "/secured/user/sell-product?error=" + URLEncoder.encode("Thêm sản phẩm thất bại.", "UTF-8"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                String errorMessage = "Lỗi máy chủ: " + e.getMessage();
                resp.sendRedirect(req.getContextPath() + "/secured/user/sell-product?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
            }
        }
    }

}
