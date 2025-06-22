package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.dto.ProblemDetailDTO;
import com.mycompany.quanlynongsan.model.Problem;
import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProblemRepository {

    public void insert(String name, String reason, int productId) {
        String sql = "INSERT INTO PROBLEM (name, reason, is_resolved, product_id, created_date) VALUES (?, ?, 0, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, reason);
            ps.setInt(3, productId);
            ps.setObject(4, LocalDateTime.now());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ProblemDetailDTO getProblemDetailById(int problemId) {
        String sql = ""
                + "SELECT p.problem_id, p.name AS problem_name, p.reason, p.is_resolved, p.created_date,\n"
                + "                       pr.product_id, pr.name AS product_name, pr.price, pr.description,\n"
                + "                       u.user_id, u.full_name, u.email, u.phone_number, u.address\n"
                + "                FROM PROBLEM p\n"
                + "                JOIN PRODUCT pr ON p.product_id = pr.product_id\n"
                + "                JOIN [USER] u ON pr.holder_id = u.user_id\n"
                + "                WHERE p.problem_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, problemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Problem problem = new Problem();
                problem.setProblemId(rs.getInt("problem_id"));
                problem.setName(rs.getString("problem_name"));
                problem.setReason(rs.getString("reason"));
                problem.setIsResolved(rs.getBoolean("is_resolved"));
                problem.setCreatedDate(rs.getTimestamp("created_date"));
                problem.setProductId(rs.getInt("product_id"));

                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("product_name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setDescription(rs.getString("description"));

                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setAddress(rs.getString("address"));

                return new ProblemDetailDTO(problem, product, user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Problem> getProblemsByIsResolvedWithPaging(boolean isResolved, int page, int size) {
        List<Problem> list = new ArrayList<>();
        String sql = "SELECT * FROM PROBLEM WHERE is_resolved = ? ORDER BY created_date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isResolved);
            ps.setInt(2, (page - 1) * size);
            ps.setInt(3, size);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProblem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countProblemsByIsResolved(boolean isResolved) {
        String sql = "SELECT COUNT(*) FROM PROBLEM WHERE is_resolved = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isResolved);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void markAsResolved(int problemId) {
        String sql = "UPDATE PROBLEM SET is_resolved = 1 WHERE problem_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, problemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Problem mapResultSetToProblem(ResultSet rs) throws SQLException {
        Problem problem = new Problem();
        problem.setProblemId(rs.getInt("problem_id"));
        problem.setProductId(rs.getInt("product_id"));
        problem.setName(rs.getString("name"));
        problem.setReason(rs.getString("reason"));
        problem.setIsResolved(rs.getBoolean("is_resolved"));
        problem.setCreatedDate(rs.getTimestamp("created_date"));
        return problem;
    }
}
