/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.OrderProduct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nghiem
 */
public class OrderProductRepository {
    
    // Lấy trung bình số sao của 1 sản phẩm
    private static final String AVG_RATE_BY_PRODUCT = "SELECT AVG(CAST(o.rate AS FLOAT)) AS avg_rate FROM [ORDER] o JOIN ORDER_PRODUCT op ON o.order_id = op.order_id WHERE op.product_id = ? AND o.rate IS NOT NULL ";

    // Lấy số lượt đánh giá của 1 sản phẩm
    private static final String COUNT_RATE_BY_PRODUCT = "SELECT COUNT(*) AS total_reviews FROM [ORDER] o JOIN ORDER_PRODUCT op ON o.order_id = op.order_id WHERE op.product_id = ? AND o.rate IS NOT NULL";

    public OrderProductRepository() {}

    // Hàm lấy trung bình số sao
    public Double getAverageRateByProductId(int productId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(AVG_RATE_BY_PRODUCT)) {
            
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg_rate");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0.0; // hoặc có thể trả về 0.0 nếu thích
    }

    // Hàm lấy số lượt đánh giá
    public int getNumberOfReviewsByProductId(int productId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_RATE_BY_PRODUCT)) {
            
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_reviews");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    
    public List<OrderSummary> getOrderSummariesByUserId(int userId) {
        List<OrderSummary> orders = new ArrayList<>();

        String sql = "SELECT " +
                     "o.order_id, o.created_date, o.status, " +
                     "SUM(p.price * op.quantity) AS total_price, " +
                     "SUM(op.quantity) AS total_products " +
                     "FROM [ORDER] o " +
                     "JOIN ORDER_PRODUCT op ON o.order_id = op.order_id " +
                     "JOIN PRODUCT p ON op.product_id = p.product_id " +
                     "WHERE o.user_id = ? " +
                     "GROUP BY o.order_id, o.created_date, o.status " +
                     "ORDER BY o.created_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                java.sql.Date createdDate = rs.getDate("created_date");
                String status = rs.getString("status");
                double totalPrice = rs.getDouble("total_price");
                int totalProducts = rs.getInt("total_products");

                OrderSummary order = new OrderSummary(orderId, createdDate, status, totalPrice, totalProducts);
                orders.add(order);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return orders;
    }
    
    public List<OrderProduct> getProductsByOrderId(int orderId) {
    List<OrderProduct> products = new ArrayList<>();

    String sql = "SELECT op.order_id, op.product_id, op.quantity " +
                 "FROM ORDER_PRODUCT op " +
                 "WHERE op.order_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, orderId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Integer oId = rs.getInt("order_id");
            Integer productId = rs.getInt("product_id");
            Integer quantity = rs.getInt("quantity");

            OrderProduct op = new OrderProduct(oId, productId, quantity);
            products.add(op);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return products;
}
    
    public OrderSummary getOrderSummaryByOrderId(int orderId) {
    String sql = "SELECT " +
                 "o.order_id, o.created_date, o.status, " +
                 "SUM(p.price * op.quantity) AS total_price, " +
                 "SUM(op.quantity) AS total_products " +
                 "FROM [ORDER] o " +
                 "JOIN ORDER_PRODUCT op ON o.order_id = op.order_id " +
                 "JOIN PRODUCT p ON op.product_id = p.product_id " +
                 "WHERE o.order_id = ? " +
                 "GROUP BY o.order_id, o.created_date, o.status";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, orderId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            java.sql.Date createdDate = rs.getDate("created_date");
            String status = rs.getString("status");
            double totalPrice = rs.getDouble("total_price");
            int totalProducts = rs.getInt("total_products");

            return new OrderSummary(orderId, createdDate, status, totalPrice, totalProducts);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return null; // nếu không tìm thấy
}


    
    public class OrderSummary {
        private int orderId;
        private String status;
        private java.sql.Date createdDate;
        private double totalPrice;
        private int totalProducts;

        public OrderSummary(int orderId, java.sql.Date createdDate, String status, double totalPrice, int totalProducts) {
            this.orderId = orderId;
            this.createdDate = createdDate;
            this.status = status;
            this.totalPrice = totalPrice;
            this.totalProducts = totalProducts;
        }

        // Getters
        public int getOrderId() { return orderId; }
        public java.sql.Date getCreatedDate() { return createdDate; }
        public String getStatus() { return status; }
        public double getTotalPrice() { return totalPrice; }
        public int getTotalProducts() { return totalProducts; }
    }

}
