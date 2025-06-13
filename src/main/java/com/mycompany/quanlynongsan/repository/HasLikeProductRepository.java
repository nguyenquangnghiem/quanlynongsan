/*
 * Click nbfs://nbhost/SystemFileTemplates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileTemplates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nghiem
 */
public class HasLikeProductRepository {
    final private String CHECK_USER_LIKED_PRODUCT = "SELECT 1 FROM HAS_LIKE_PRODUCT WHERE user_id = ? AND product_id = ?";
    final private String INSERT_LIKE_PRODUCT = "INSERT INTO HAS_LIKE_PRODUCT(user_id, product_id) VALUES (?, ?)";
    final private String DELETE_LIKE_PRODUCT = "DELETE FROM HAS_LIKE_PRODUCT WHERE user_id = ? AND product_id = ?";

    public HasLikeProductRepository() {
    }

    // ✅ 1. Hàm kiểm tra người dùng có yêu thích sản phẩm hay không
    public boolean isProductFavorited(int userId, int productId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_USER_LIKED_PRODUCT)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Có bản ghi → đã yêu thích
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false; // Không có bản ghi hoặc lỗi → chưa yêu thích
    }

    // ✅ 2. Hàm thêm sản phẩm yêu thích cho người dùng
    public boolean addFavoriteProduct(int userId, int productId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_LIKE_PRODUCT)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0; // Thêm thành công nếu số dòng ảnh hưởng > 0
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false; // Thêm thất bại
    }
 
    
    public List<Integer> findProductIdsByUserId(int userId) {
    final String QUERY = "SELECT product_id FROM HAS_LIKE_PRODUCT WHERE user_id = ?";
    List<Integer> productIds = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(QUERY)) {

        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            productIds.add(rs.getInt("product_id"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return productIds;
}
    
    public boolean removeFavoriteProduct(int userId, int productId) {
    final String DELETE_QUERY = "DELETE FROM HAS_LIKE_PRODUCT WHERE user_id = ? AND product_id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY)) {

        stmt.setInt(1, userId);
        stmt.setInt(2, productId);
        return stmt.executeUpdate() > 0; // Xóa thành công nếu số dòng ảnh hưởng > 0
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Xóa thất bại
}
}
