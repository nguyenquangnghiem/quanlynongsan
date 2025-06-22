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
import com.mycompany.quanlynongsan.model.Category;

/**
 *
 * @author nghiem
 */

public class CategoryRepository {
    final private String FIND_ALL = "SELECT * FROM CATEGORY ";

    final private String FIND_CATEGORY_BY_PRODUCT_ID = "SELECT c.category_id, c.name FROM CATEGORY c JOIN PRODUCT_CATEGORY pc ON c.category_id = pc.category_id WHERE pc.product_id = ?";

    final private String FIND_BY_ID = "SELECT * FROM CATEGORY WHERE category_id = ?";

    public CategoryRepository() {
    }

    public List<Category> findAll() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
            ResultSet rs = stmt.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(new Category(rs.getInt("category_id"), rs.getString("name")));
            }
            return categories;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return List.of();
    }

    // ✅ Hàm lấy các category của 1 sản phẩm theo product_id
    public List<Category> findCategoriesByProductId(int productId) {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(FIND_CATEGORY_BY_PRODUCT_ID)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                categories.add(new Category(rs.getInt("category_id"), rs.getString("name")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return categories;
    }

    public Category findById(int categoryId) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Category(rs.getInt("category_id"), rs.getString("name"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null; // Không tìm thấy trả về null
    }
}
