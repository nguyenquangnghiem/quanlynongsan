package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.response.BehaviorLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BehaviorLogRepository {

    // Lấy danh sách hành vi với phân trang
    public List<BehaviorLog> findBehaviorLogs(int page, int pageSize) {
        List<BehaviorLog> list = new ArrayList<>();
        String sql = ""
                + "SELECT hb.user_id, u.full_name, u.email, hb.behavior_id, b.code, b.description, hb.time\n"
                + "            FROM HAS_BEHAVIOR hb\n"
                + "            JOIN [USER] u ON hb.user_id = u.user_id\n"
                + "            JOIN BEHAVIOR b ON hb.behavior_id = b.behavior_id\n"
                + "            ORDER BY hb.time DESC\n"
                + "            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (page - 1) * pageSize);
            ps.setInt(2, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BehaviorLog log = new BehaviorLog();
                    log.setUserId(rs.getInt("user_id"));
                    log.setFullName(rs.getString("full_name"));
                    log.setEmail(rs.getString("email"));
                    log.setBehaviorId(rs.getInt("behavior_id"));
                    log.setCode(rs.getString("code"));
                    log.setDescription(rs.getString("description"));
                    log.setTime(rs.getTimestamp("time").toLocalDateTime());
                    list.add(log);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Đếm tổng số bản ghi để phục vụ phân trang
    public int countTotalBehaviorLogs() {
        String sql = "SELECT COUNT(*) FROM HAS_BEHAVIOR";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
