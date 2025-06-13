/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.Delivery;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
}
