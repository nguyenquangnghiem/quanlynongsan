package com.mycompany.quanlynongsan.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mycompany.quanlynongsan.config.CloudinaryConfig;
import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.ImageProduct;
import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.CategoryRepository;
import com.mycompany.quanlynongsan.repository.ImageProductRepository;
import com.mycompany.quanlynongsan.repository.ProductRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@MultipartConfig
@WebServlet(urlPatterns = "/secured/user/edit-product")
public class EditProductServlet extends HttpServlet {

    private final ProductRepository productRepository = new ProductRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final ImageProductRepository imageProductRepository = new ImageProductRepository();
    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int productId = Integer.parseInt(req.getParameter("id"));
        Product product = productRepository.findByIdNonIsBrowseNonIsSell(productId);
        List<com.mycompany.quanlynongsan.model.Category> allCategories = categoryRepository.findAll();
        List<com.mycompany.quanlynongsan.model.Category> selectedCategoryIds = categoryRepository.findCategoriesByProductId(productId);
        List<ImageProduct> imageProducts = imageProductRepository.findByProductId(productId);

        req.setAttribute("product", product);
        req.setAttribute("allCategories", allCategories);
        req.setAttribute("selectedCategoryIds", selectedCategoryIds);

        List<String> imageUrls = new ArrayList<>();
        for (ImageProduct imageProduct : imageProducts) {
            imageUrls.add(imageProduct.getUrlImage());
        }
        req.setAttribute("imageUrls", imageUrls);
        req.getRequestDispatcher("/user/edit-product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String redirectURL = req.getContextPath() + "/secured/user/my-products";
        if (user.getRoleId() == 2) {
            redirectURL = req.getContextPath() + "/secured/user/my-stock";
        }

        try {
            // Lấy thông tin sản phẩm từ request
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            BigDecimal price = new BigDecimal(req.getParameter("price"));
            Integer quantity = Integer.parseInt(req.getParameter("quantity"));
            String status = req.getParameter("status");
            Boolean isSell = Boolean.parseBoolean(req.getParameter("is_sell"));
            Boolean isBrowse = Boolean.parseBoolean(req.getParameter("is_browse"));
            String placeOfManufacture = req.getParameter("place_of_manufacture");
            String[] categoryIdsParam = req.getParameterValues("category_ids");
            Integer productId = Integer.valueOf(req.getParameter("product_id"));
            Boolean isActive = Boolean.parseBoolean(req.getParameter("is_active"));
            Integer holderId = user.getUserId();
            Date createdDate = new Date();

            Integer[] categories = null;
            if (categoryIdsParam != null) {
                categories = new Integer[categoryIdsParam.length];
                for (int i = 0; i < categoryIdsParam.length; i++) {
                    categories[i] = Integer.parseInt(categoryIdsParam[i]);
                }
            }

            Product product = new Product(productId, name, description, price, quantity, status, isSell, isBrowse,
                    placeOfManufacture, isActive, holderId, createdDate);

            // Upload ảnh
            Collection<Part> fileParts = req.getParts();
            List<String> imageUrls = new ArrayList<>();
            Cloudinary cloudinary = CloudinaryConfig.getInstance();

            for (Part filePart : fileParts) {
                String fileName = filePart.getSubmittedFileName();
                if (fileName != null && !fileName.isEmpty()) {
                    File tempFile = File.createTempFile("upload-", fileName);
                    try (InputStream fileContent = filePart.getInputStream();
                         OutputStream out = new FileOutputStream(tempFile)) {
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
                    } finally {
                        tempFile.delete();
                    }
                }
            }

            boolean success = productRepository.update(product, imageUrls, categories);
            if (success) {
                Behavior behavior = behaviorRepository.findByCode("EDIT_PRODUCT");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                String successMsg = URLEncoder.encode("Cập nhật sản phẩm thành công!", "UTF-8");
                resp.sendRedirect(redirectURL + "?success=" + successMsg);
            } else {
                String errorMsg = URLEncoder.encode("❌ Cập nhật sản phẩm thất bại!", "UTF-8");
                resp.sendRedirect(redirectURL + "?error=" + errorMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = URLEncoder.encode("❌ Lỗi server: " + e.getMessage(), "UTF-8");
            resp.sendRedirect(redirectURL + "?error=" + errorMsg);
        }
    }
}
