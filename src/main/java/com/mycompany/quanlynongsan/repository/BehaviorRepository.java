/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.Behavior;

/**
 *
 * @author nghiem
 */
public class BehaviorRepository {

    public BehaviorRepository() {
    }

    public Behavior findByCode(String code) {
        String sql = "SELECT behavior_id, code, description FROM BEHAVIOR WHERE code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Behavior b = new Behavior();
                    b.setBehaviorId(rs.getInt("behavior_id"));
                    b.setCode(rs.getString("code"));
                    b.setDescription(rs.getString("description"));
                    return b;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean insertLog(int userId, int behaviorId) {
        String sql = "INSERT INTO HAS_BEHAVIOR(user_id, behavior_id, time) VALUES (?, ?, GETDATE())";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, behaviorId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
