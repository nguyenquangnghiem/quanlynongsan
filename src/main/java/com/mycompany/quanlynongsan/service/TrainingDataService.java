/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.service;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class TrainingDataService {

    public String getTrainingData() {
        StringBuilder trainingData = new StringBuilder();

        try (Connection conn = DatabaseConnection.getConnection()) {

            Statement stmt = conn.createStatement();

            // 1. Danh sách Role
        ResultSet rs = stmt.executeQuery("SELECT role_id, name FROM ROLE");
        trainingData.append("=== DANH SÁCH ROLE ===\n");
        while (rs.next()) {
            trainingData.append("Role ID: ").append(rs.getInt("role_id")).append("\n")
                    .append("Tên quyền: ").append(rs.getString("name")).append("\n\n");
        }

        // 2. Danh sách danh mục sản phẩm
        rs = stmt.executeQuery("SELECT category_id, name FROM CATEGORY");
        trainingData.append("=== DANH SÁCH DANH MỤC SẢN PHẨM ===\n");
        while (rs.next()) {
            trainingData.append("Danh mục ID: ").append(rs.getInt("category_id")).append("\n")
                    .append("Tên danh mục: ").append(rs.getString("name")).append("\n\n");
        }

        // 3. Trung bình đánh giá của từng sản phẩm
        rs = stmt.executeQuery(
            "SELECT p.product_id, p.name, AVG(CAST(o.rate AS FLOAT)) AS avg_rate, COUNT(DISTINCT o.order_id) AS total_orders " +
            "FROM PRODUCT p " +
            "JOIN ORDER_PRODUCT op ON p.product_id = op.product_id " +
            "JOIN [ORDER] o ON op.order_id = o.order_id " +
            "WHERE o.rate IS NOT NULL " +
            "GROUP BY p.product_id, p.name"
        );
        trainingData.append("=== TRUNG BÌNH ĐÁNH GIÁ CỦA TỪNG SẢN PHẨM ===\n");
        while (rs.next()) {
            trainingData.append("Sản phẩm ID: ").append(rs.getInt("product_id")).append("\n")
                    .append("Tên sản phẩm: ").append(rs.getString("name")).append("\n")
                    .append("Trung bình đánh giá: ").append(String.format("%.2f", rs.getDouble("avg_rate"))).append(" / 5").append("\n")
                    .append("Tổng số đơn hàng có đánh giá: ").append(rs.getInt("total_orders")).append("\n\n");
        }

        // 1. Thông tin người dùng
        rs = stmt.executeQuery("SELECT user_id, full_name, email, phone_number, address, created_date FROM [USER] WHERE is_active = 1");
        trainingData.append("=== DANH SÁCH NGƯỜI DÙNG ===\n");
        while (rs.next()) {
            trainingData.append("Người dùng ID: ").append(rs.getInt("user_id")).append("\n")
                    .append("Tên: ").append(rs.getString("full_name")).append("\n")
                    .append("Email: ").append(rs.getString("email")).append("\n")
                    .append("SĐT: ").append(rs.getString("phone_number")).append("\n")
                    .append("Địa chỉ: ").append(rs.getString("address")).append("\n")
                    .append("Ngày tạo: ").append(rs.getTimestamp("created_date")).append("\n\n");
        }

        // 2. Thông tin sản phẩm kèm theo danh mục
        rs = stmt.executeQuery(
            "SELECT p.product_id, p.name AS product_name, p.description, p.price, p.quantity, p.status, " +
            "c.name AS category_name, p.place_of_manufacture " +
            "FROM PRODUCT p " +
            "LEFT JOIN PRODUCT_CATEGORY pc ON p.product_id = pc.product_id " +
            "LEFT JOIN CATEGORY c ON pc.category_id = c.category_id " +
            "WHERE p.is_active = 1"
        );
        trainingData.append("=== DANH SÁCH SẢN PHẨM ===\n");
        while (rs.next()) {
            trainingData.append("Sản phẩm ID: ").append(rs.getInt("product_id")).append("\n")
                    .append("Tên: ").append(rs.getString("product_name")).append("\n")
                    .append("Danh mục: ").append(rs.getString("category_name")).append("\n")
                    .append("Giá: ").append(rs.getDouble("price")).append(" VNĐ\n")
                    .append("Mô tả: ").append(rs.getString("description")).append("\n")
                    .append("Số lượng tồn: ").append(rs.getInt("quantity")).append("\n")
                    .append("Nơi sản xuất: ").append(rs.getString("place_of_manufacture")).append("\n")
                    .append("Trạng thái: ").append(rs.getString("status")).append("\n\n");
        }

        // 3. Đơn hàng chi tiết + giao hàng
        rs = stmt.executeQuery(
            "SELECT o.order_id, u.full_name, o.status, o.payment_method, o.rate, o.created_date, " +
            "d.address AS delivery_address, " +
            "p.product_id, p.name AS product_name, op.quantity " +
            "FROM [ORDER] o " +
            "JOIN [USER] u ON o.user_id = u.user_id " +
            "LEFT JOIN ORDER_DELIVERY od ON o.order_id = od.order_id " +
            "LEFT JOIN DELIVERY d ON od.delivery_id = d.delivery_id " +
            "JOIN ORDER_PRODUCT op ON o.order_id = op.order_id " +
            "JOIN PRODUCT p ON op.product_id = p.product_id " +
            "ORDER BY o.order_id DESC"
        );
        trainingData.append("=== DANH SÁCH ĐƠN HÀNG ===\n");

        Map<Integer, StringBuilder> orderMap = new LinkedHashMap<>();
        while (rs.next()) {
            int orderId = rs.getInt("order_id");
            orderMap.putIfAbsent(orderId, new StringBuilder());
            StringBuilder orderDetail = orderMap.get(orderId);

            if (orderDetail.length() == 0) {
                orderDetail.append("Đơn hàng ID: ").append(orderId).append("\n")
                        .append("Khách hàng: ").append(rs.getString("full_name")).append("\n")
                        .append("Trạng thái: ").append(rs.getString("status")).append("\n")
                        .append("Phương thức thanh toán: ").append(rs.getString("payment_method")).append("\n")
                        .append("Đánh giá: ").append(rs.getInt("rate")).append("\n")
                        .append("Ngày tạo: ").append(rs.getTimestamp("created_date")).append("\n")
                        .append("Địa chỉ giao hàng: ").append(rs.getString("delivery_address")).append("\n")
                        .append("Danh sách sản phẩm:\n");
            }

            orderDetail.append("   - Sản phẩm ID: ").append(rs.getInt("product_id"))
                    .append(" | Tên: ").append(rs.getString("product_name"))
                    .append(" | Số lượng: ").append(rs.getInt("quantity")).append("\n");
        }
        for (StringBuilder od : orderMap.values()) {
            trainingData.append(od).append("\n");
        }

        // 4. Phản hồi từ khách hàng
        rs = stmt.executeQuery("SELECT contact_id, full_name, phone_number, description, created_date FROM CONTACT ORDER BY created_date DESC");
        trainingData.append("=== PHẢN HỒI KHÁCH HÀNG ===\n");
        while (rs.next()) {
            trainingData.append("Phản hồi ID: ").append(rs.getInt("contact_id")).append("\n")
                    .append("Tên: ").append(rs.getString("full_name")).append("\n")
                    .append("SĐT: ").append(rs.getString("phone_number")).append("\n")
                    .append("Nội dung: ").append(rs.getString("description")).append("\n")
                    .append("Ngày gửi: ").append(rs.getTimestamp("created_date")).append("\n\n");
        }

        // 5. Vấn đề sản phẩm
        rs = stmt.executeQuery("SELECT problem_id, name, reason, is_resolved, created_date FROM PROBLEM ORDER BY created_date DESC");
        trainingData.append("=== VẤN ĐỀ SẢN PHẨM ===\n");
        while (rs.next()) {
            trainingData.append("Vấn đề ID: ").append(rs.getInt("problem_id")).append("\n")
                    .append("Tên vấn đề: ").append(rs.getString("name")).append("\n")
                    .append("Lý do: ").append(rs.getString("reason")).append("\n")
                    .append("Đã xử lý: ").append(rs.getBoolean("is_resolved") ? "Đã xử lý" : "Chưa xử lý").append("\n")
                    .append("Ngày tạo: ").append(rs.getTimestamp("created_date")).append("\n\n");
        }

        // 6. Yêu cầu trả hàng
        rs = stmt.executeQuery("SELECT return_request_id, reason, status, created_date FROM RETURN_REQUEST ORDER BY created_date DESC");
        trainingData.append("=== YÊU CẦU TRẢ HÀNG ===\n");
        while (rs.next()) {
            trainingData.append("Yêu cầu ID: ").append(rs.getInt("return_request_id")).append("\n")
                    .append("Lý do: ").append(rs.getString("reason")).append("\n")
                    .append("Trạng thái: ").append(rs.getString("status")).append("\n")
                    .append("Ngày gửi: ").append(rs.getTimestamp("created_date")).append("\n\n");
        }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Không thể lấy dữ liệu huấn luyện: " + e.getMessage();
        }

        return trainingData.toString();
    }
}
