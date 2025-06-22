/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.response.CartSummaryResponse;
import com.mycompany.quanlynongsan.model.HasCart;
import com.mycompany.quanlynongsan.model.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HasCartRepository {

    private static final String CHECK_PRODUCT_IN_CART = 
        "SELECT quantity FROM HAS_CART WHERE user_id = ? AND product_id = ?";

    private static final String INSERT_PRODUCT_INTO_CART = 
        "INSERT INTO HAS_CART (user_id, product_id, quantity) VALUES (?, ?, ?)";

    private static final String UPDATE_PRODUCT_QUANTITY = 
        "UPDATE HAS_CART SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
    
    private static final String GET_HAS_CART_BY_USER_ID = 
        "SELECT * FROM HAS_CART WHERE user_id = ? ";

    public HasCartRepository() {
    }

    /**
     * Thêm sản phẩm vào giỏ hàng. Nếu sản phẩm đã có thì tăng số lượng.
     * @param userId ID người dùng
     * @param productId ID sản phẩm
     * @param quantity số lượng muốn thêm
     */
    public void addToCart(int userId, int productId, int quantity) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // 1. Kiểm tra xem sản phẩm đã có trong giỏ chưa
            try (PreparedStatement checkStmt = conn.prepareStatement(CHECK_PRODUCT_IN_CART)) {
                checkStmt.setInt(1, userId);
                checkStmt.setInt(2, productId);

                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // ✅ Đã có sản phẩm trong giỏ → cập nhật số lượng
                    try (PreparedStatement updateStmt = conn.prepareStatement(UPDATE_PRODUCT_QUANTITY)) {
                        updateStmt.setInt(1, quantity);
                        updateStmt.setInt(2, userId);
                        updateStmt.setInt(3, productId);
                        updateStmt.executeUpdate();
                    }

                } else {
                    // ✅ Chưa có sản phẩm → thêm mới
                    try (PreparedStatement insertStmt = conn.prepareStatement(INSERT_PRODUCT_INTO_CART)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, productId);
                        insertStmt.setInt(3, quantity);
                        insertStmt.executeUpdate();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<HasCart> getHasCartByUserId(int userId) {
        List<HasCart> hasCarts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_HAS_CART_BY_USER_ID)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                hasCarts.add(new HasCart(userId, productId, quantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasCarts;
    }
    
    /**
    * Xóa sản phẩm khỏi giỏ hàng của người dùng
    * @param userId ID người dùng
    * @param productId ID sản phẩm cần xóa
    */
    public void removeProductFromCart(int userId, int productId) {
        String sql = "DELETE FROM HAS_CART WHERE user_id = ? AND product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
