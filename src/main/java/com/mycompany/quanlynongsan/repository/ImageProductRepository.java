/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.ImageProduct;

/**
 *
 * @author nghiem
 */
public class ImageProductRepository {
    final private String FIND_BY_PRODUCT_ID = "SELECT * FROM IMAGE_PRODUCT ip WHERE ip.product_id = ? ";

    public ImageProductRepository() {
    }

    public List<ImageProduct> findByProductId(Integer productId) {
        List<ImageProduct> images = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(FIND_BY_PRODUCT_ID);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                images.add(new ImageProduct(
                        rs.getInt("image_product_id"),
                        rs.getInt("product_id"),
                        rs.getString("url_image")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return images;
    }
}
