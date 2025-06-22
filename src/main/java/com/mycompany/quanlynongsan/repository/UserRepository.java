/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.response.UserWithRole;

/**
 *
 * @author nghiem
 */
public class UserRepository {

    final private String CREATE_USER = "INSERT INTO [USER](email, password, role_id, is_active, created_date) VALUES (?,?,?,?,?) ";

    final private String UPDATE_USER = "UPDATE [USER] SET full_name = ?, address = ?, phone_number = ? WHERE user_id = ? ";

    final private String FIND_BY_EMAIL = "SELECT * FROM [USER] WHERE email = ? AND is_active = 1";

    final private String FIND_BY_ID = "SELECT * FROM [USER] WHERE user_id = ? AND is_active = 1";

    final private String FIND_BY_ID_AND_NON_IS_ACTIVE = "SELECT * FROM [USER] WHERE user_id = ?";

    public UserRepository() {
    }

    public User findById(int userId) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer id = rs.getInt("user_id");
                    String fullName = rs.getString("full_name");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String phoneNumber = rs.getString("phone_number");
                    String address = rs.getString("address");
                    Boolean isActive = rs.getBoolean("is_active");
                    Integer roleId = rs.getInt("role_id");
                    java.sql.Timestamp createdDateSql = rs.getTimestamp("created_date");
                    Date createdDate = createdDateSql != null ? new Date(createdDateSql.getTime()) : null;

                    return new User(id, fullName, email, password, phoneNumber, address, isActive, roleId, createdDate);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void updatePassword(String email, String newPassword) throws SQLException {
        String sql = "UPDATE [USER] SET password = ? WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt())); // 👉 Có thể thêm hash password nếu muốn
            ps.setString(2, email);
            ps.executeUpdate();
        }
    }

    public User findByIdAndNonIsActive(int userId) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID_AND_NON_IS_ACTIVE)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer id = rs.getInt("user_id");
                    String fullName = rs.getString("full_name");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String phoneNumber = rs.getString("phone_number");
                    String address = rs.getString("address");
                    Boolean isActive = rs.getBoolean("is_active");
                    Integer roleId = rs.getInt("role_id");
                    java.sql.Timestamp createdDateSql = rs.getTimestamp("created_date");
                    Date createdDate = createdDateSql != null ? new Date(createdDateSql.getTime()) : null;

                    return new User(id, fullName, email, password, phoneNumber, address, isActive, roleId, createdDate);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean save(User user) {
        final String CREATE_USER = "INSERT INTO [USER](email, password, role_id, is_active, created_date) VALUES (?,?,?,?,?)";
        final String UPDATE_USER = "UPDATE [USER] SET full_name = ?, address = ?, phone_number = ? WHERE user_id = ?";
        final String UPDATE_PASSWORD = "UPDATE [USER] SET password = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (user.getUserId() == null) {
                // Thêm mới
                PreparedStatement stmtCreate = conn.prepareStatement(CREATE_USER);
                stmtCreate.setString(1, user.getEmail());
                stmtCreate.setString(2, user.getPassword());
                stmtCreate.setInt(3, user.getRoleId());
                stmtCreate.setBoolean(4, true);
                stmtCreate.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                stmtCreate.executeUpdate();
                return true;
            } else {
                // Cập nhật thông tin cá nhân
                PreparedStatement stmtUpdate = conn.prepareStatement(UPDATE_USER);
                stmtUpdate.setString(1, user.getFullName());
                stmtUpdate.setString(2, user.getAddress());
                stmtUpdate.setString(3, user.getPhoneNumber());
                stmtUpdate.setInt(4, user.getUserId());
                stmtUpdate.executeUpdate();

                // Nếu có mật khẩu mới, cập nhật thêm mật khẩu
                if (user.getPassword() != null && !user.getPassword().isBlank()) {
                    PreparedStatement stmtUpdatePassword = conn.prepareStatement(UPDATE_PASSWORD);
                    stmtUpdatePassword.setString(1, user.getPassword());
                    stmtUpdatePassword.setInt(2, user.getUserId());
                    stmtUpdatePassword.executeUpdate();
                }
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public User findByEmail(String email) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(FIND_BY_EMAIL)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer userId = Integer.valueOf(rs.getString("user_id"));
                    String fullName = rs.getString("full_name");
                    String emailOld = rs.getString("email");
                    String password = rs.getString("password");
                    String phoneNumber = rs.getString("phone_number");
                    String address = rs.getString("address");
                    Boolean isActive = rs.getBoolean("is_active");
                    Integer roleId = rs.getInt("role_id");
                    java.sql.Date createdDateSql = rs.getDate("created_date");
                    Date createdDate = new Date(createdDateSql.getTime());
                    return new User(userId, fullName, emailOld, password, phoneNumber, address, isActive, roleId,
                            createdDate);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<UserWithRole> findAllWithRole() {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.phone_number, u.address, u.is_active, "
                + "u.role_id, r.name AS role_name, u.created_date "
                + "FROM [USER] u "
                + "JOIN ROLE r ON u.role_id = r.role_id";

        List<UserWithRole> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UserWithRole user = new UserWithRole();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setAddress(rs.getString("address"));
                user.setIsActive(rs.getBoolean("is_active"));
                user.setRoleId(rs.getInt("role_id"));
                user.setRoleName(rs.getString("role_name"));
                Timestamp createdDateSql = rs.getTimestamp("created_date");
                user.setCreatedDate(createdDateSql != null ? new Date(createdDateSql.getTime()) : null);

                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return users;
    }

    public List<UserWithRole> findAllWithRole(int roleId) {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.phone_number, u.address, u.is_active, "
                + "u.role_id, r.name AS role_name, u.created_date "
                + "FROM [USER] u "
                + "JOIN ROLE r ON u.role_id = r.role_id "
                + "WHERE u.role_id = ?";

        List<UserWithRole> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UserWithRole user = new UserWithRole();
                    user.setUserId(rs.getInt("user_id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                    user.setAddress(rs.getString("address"));
                    user.setIsActive(rs.getBoolean("is_active"));
                    user.setRoleId(rs.getInt("role_id"));
                    user.setRoleName(rs.getString("role_name"));
                    Timestamp createdDateSql = rs.getTimestamp("created_date");
                    user.setCreatedDate(createdDateSql != null ? new Date(createdDateSql.getTime()) : null);

                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return users;
    }

}
