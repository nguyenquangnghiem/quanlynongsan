/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.ImageProduct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nghiem
 */
public class ImageProductRepository {
    final private String FIND_1 = "SELECT TOP 1 *\n" +
"FROM IMAGE_PRODUCT ip\n" +
"JOIN PRODUCT p ON p.product_id = ip.product_id\n" +
"WHERE p.product_id = ?;";
    
    final private String FIND_ALL = "SELECT * FROM IMAGE_PRODUCT WHERE product_id = ?";

    public ImageProductRepository() {
    }
    
    public ImageProduct find1(Integer productId) {
        try (Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(FIND_1);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                return new ImageProduct(rs.getInt("image_product_id"), rs.getInt("product_id"), rs.getString("url_image"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
     public List<ImageProduct> findAllByProductId(Integer productId) {
        List<ImageProduct> images = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                images.add(new ImageProduct(
                        rs.getInt("image_product_id"),
                        rs.getInt("product_id"),
                        rs.getString("url_image")
                ));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return images;
    }
}
