/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.Delivery;

/**
 *
 * @author nghiem
 */
public class DeliveryRepository {

    public DeliveryRepository() {
    }

    public List<Delivery> getDeliveriesByOrderId(int orderId) throws Exception {
        List<Delivery> deliveries = new ArrayList<>();

        String sql = "SELECT d.delivery_id, d.address, d.created_date FROM DELIVERY d JOIN ORDER_DELIVERY od ON d.delivery_id = od.delivery_id WHERE od.order_id = ? ORDER BY d.created_date DESC ";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Delivery delivery = new Delivery();
                    delivery.setDeliveryId(rs.getInt("delivery_id"));
                    delivery.setAddress(rs.getString("address"));
                    Timestamp timestamp = rs.getTimestamp("created_date");
                    delivery.setCreatedDate(timestamp != null ? new Date(timestamp.getTime()) : null);
                    deliveries.add(delivery);
                }
            }
        }

        return deliveries;
    }

    public void insert(int orderId, String address) {
        String INSERT_DELIVERY_SQL = "INSERT INTO DELIVERY (address, created_date) VALUES (?, GETDATE())";
        String INSERT_ORDER_DELIVERY_SQL = "INSERT INTO ORDER_DELIVERY (order_id, delivery_id) VALUES (?, ?)";

        try (var conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement insertDeliveryStmt = conn.prepareStatement(INSERT_DELIVERY_SQL,
                    PreparedStatement.RETURN_GENERATED_KEYS);
                    PreparedStatement insertOrderDeliveryStmt = conn.prepareStatement(INSERT_ORDER_DELIVERY_SQL)) {

                // Thêm vào bảng DELIVERY
                insertDeliveryStmt.setString(1, address);
                insertDeliveryStmt.executeUpdate();

                // Lấy delivery_id vừa tạo
                ResultSet generatedKeys = insertDeliveryStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int deliveryId = generatedKeys.getInt(1);

                    // Gắn vào ORDER_DELIVERY
                    insertOrderDeliveryStmt.setInt(1, orderId);
                    insertOrderDeliveryStmt.setInt(2, deliveryId);
                    insertOrderDeliveryStmt.executeUpdate();
                } else {
                    throw new RuntimeException("Không lấy được delivery_id sau khi insert");
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Thêm lịch sử vận chuyển thất bại: " + ex.getMessage());
        }
    }

}
