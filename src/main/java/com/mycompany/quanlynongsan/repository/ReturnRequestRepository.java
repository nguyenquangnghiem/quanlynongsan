package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.ReturnRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnRequestRepository {

    public void create(ReturnRequest request) {
        String sql = "INSERT INTO RETURN_REQUEST (order_id, user_id, reason, status, created_date) VALUES (?, ?, ?, 'PENDING', GETDATE())";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, request.getOrderId());
            stmt.setInt(2, request.getUserId());
            stmt.setString(3, request.getReason());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ReturnRequest> getByUserId(int userId) {
        List<ReturnRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM RETURN_REQUEST WHERE user_id = ? ORDER BY created_date DESC";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ReturnRequest req = mapRow(rs);
                list.add(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ReturnRequest mapRow(ResultSet rs) throws SQLException {
        ReturnRequest req = new ReturnRequest();
        req.setReturnRequestId(rs.getInt("return_request_id"));
        req.setOrderId(rs.getInt("order_id"));
        req.setUserId(rs.getInt("user_id"));
        req.setReason(rs.getString("reason"));
        req.setStatus(rs.getString("status"));
        req.setCreatedDate(rs.getTimestamp("created_date"));
        req.setResolvedDate(rs.getTimestamp("resolved_date"));
        return req;
    }

    public List<ReturnRequest> getRequestsBySellerId(int sellerId) {
        List<ReturnRequest> list = new ArrayList<>();
        String sql = ""
                + "SELECT DISTINCT rr.*\n"
                + "        FROM RETURN_REQUEST rr\n"
                + "        JOIN ORDER_PRODUCT op ON rr.order_id = op.order_id\n"
                + "        JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "        WHERE p.holder_id = ?\n"
                + "        ORDER BY rr.created_date DESC";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateStatus(int returnRequestId, String newStatus, java.util.Date resolvedDate) {
        String sql = "UPDATE RETURN_REQUEST SET status = ?, resolved_date = ? WHERE return_request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setTimestamp(2, new Timestamp(resolvedDate.getTime()));
            stmt.setInt(3, returnRequestId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean acceptReturnRequest(int returnRequestId, java.util.Date resolvedDate) {
        String updateReturnSql = "UPDATE RETURN_REQUEST SET status = ?, resolved_date = ? WHERE return_request_id = ?";
        String updateOrderSql = "UPDATE [ORDER] SET status = 'CONFIRMED' WHERE order_id = (SELECT order_id FROM RETURN_REQUEST WHERE return_request_id = ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Cập nhật trạng thái yêu cầu hoàn trả
            try (PreparedStatement stmt = conn.prepareStatement(updateReturnSql)) {
                stmt.setString(1, "ACCEPTED"); // Hoặc "CONFIRMED" nếu bạn muốn thống nhất
                stmt.setTimestamp(2, new Timestamp(resolvedDate.getTime()));
                stmt.setInt(3, returnRequestId);
                stmt.executeUpdate();
            }

            // Cập nhật trạng thái đơn hàng thành CONFIRMED
            try (PreparedStatement stmt = conn.prepareStatement(updateOrderSql)) {
                stmt.setInt(1, returnRequestId);
                stmt.executeUpdate();
            }

            conn.commit(); // Commit nếu mọi thứ OK
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
