package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.Order;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderRepository {

    private static final String FIND_DISTINCT_ORDERS_BY_PRODUCT_ID = "SELECT DISTINCT o.order_id, o.estimated_time, o.comment, o.status, o.rate, o.user_id, o.created_date FROM [ORDER] o JOIN ORDER_PRODUCT op ON o.order_id = op.order_id WHERE op.product_id = ? AND o.rate IS NOT NULL";

    public OrderRepository() {
    }

    // ✅ Lấy danh sách đơn hàng distinct có chứa productId
    public List<Order> findDistinctOrdersByProductId(int productId) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_DISTINCT_ORDERS_BY_PRODUCT_ID)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getTimestamp("estimated_time"),
                        rs.getString("comment"),
                        rs.getString("status"),
                        rs.getInt("rate"),
                        rs.getString("payment_method"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("created_date")
                );
                orders.add(order);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return orders;
    }
    
    public Order getById(int orderId) {
    String GET_ORDER_BY_ID_SQL = "SELECT order_id, estimated_time, comment, status, rate, payment_method, user_id, created_date FROM [ORDER] WHERE order_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(GET_ORDER_BY_ID_SQL)) {

        stmt.setInt(1, orderId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Order(
                    rs.getInt("order_id"),
                    rs.getTimestamp("estimated_time"),
                    rs.getString("comment"),
                    rs.getString("status"),
                    rs.getObject("rate") != null ? rs.getInt("rate") : null,
                    rs.getString("payment_method"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("created_date")
            );
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null; // Không tìm thấy
}

    
    public void save(Order order) {
    String INSERT_ORDER_SQL = "INSERT INTO [ORDER] (estimated_time, comment, status, rate, payment_method, user_id, created_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(INSERT_ORDER_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

        stmt.setTimestamp(1, new java.sql.Timestamp(order.getEstimatedTime().getTime()));
        stmt.setString(2, order.getComment());
        stmt.setString(3, order.getStatus());
        if (order.getRate() != null) {
            stmt.setInt(4, order.getRate());
        } else {
            stmt.setNull(4, java.sql.Types.INTEGER);
        }
        stmt.setString(5, order.getPaymentMethod());
        stmt.setInt(6, order.getUserId());
        stmt.setTimestamp(7, new java.sql.Timestamp(order.getCreatedDate().getTime()));

        stmt.executeUpdate();

        // Lấy ID tự sinh (nếu cần)
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            order.setOrderId(generatedKeys.getInt(1));
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}
    
    public void transferCartToOrder(int userId, int orderId) {
    String findCartSQL = "SELECT product_id, quantity FROM HAS_CART WHERE user_id = ?";
    String insertOrderProductSQL = "INSERT INTO ORDER_PRODUCT (order_id, product_id, quantity) VALUES (?, ?, ?)";
    String deleteCartSQL = "DELETE FROM HAS_CART WHERE user_id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false); // ✅ Transaction

        // Lấy sản phẩm trong giỏ
        try (PreparedStatement findCartStmt = conn.prepareStatement(findCartSQL)) {
            findCartStmt.setInt(1, userId);
            ResultSet rs = findCartStmt.executeQuery();

            try (PreparedStatement insertStmt = conn.prepareStatement(insertOrderProductSQL)) {
                while (rs.next()) {
                    insertStmt.setInt(1, orderId);
                    insertStmt.setInt(2, rs.getInt("product_id"));
                    insertStmt.setInt(3, rs.getInt("quantity"));
                    insertStmt.addBatch(); // ✅ Thêm batch
                }
                insertStmt.executeBatch(); // ✅ Thực thi batch insert
            }
        }

        // Xóa giỏ hàng
        try (PreparedStatement deleteCartStmt = conn.prepareStatement(deleteCartSQL)) {
            deleteCartStmt.setInt(1, userId);
            deleteCartStmt.executeUpdate();
        }

        conn.commit(); // ✅ Commit transaction

    } catch (Exception ex) {
        ex.printStackTrace();
        throw new RuntimeException("Chuyển giỏ hàng sang đơn hàng thất bại");
    }
}
    
    public void updateRateAndComment(int orderId, int rate, String comment) {
        String UPDATE_SQL = "UPDATE [ORDER] SET rate = ?, comment = ?, status = ? WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setInt(1, rate);
            stmt.setString(2, comment);
            stmt.setString(3, "REVIEWED");
            stmt.setInt(4, orderId);

            stmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cập nhật đánh giá đơn hàng thất bại.");
        }
    }
    
    public static List<OrderSummary> getPendingOrdersBySeller(int sellerId) {
    List<OrderSummary> list = new ArrayList<>();
    final String SQL = " SELECT o.order_id, o.created_date, o.total_price, u.full_name, u.phone_number, u.address FROM [ORDER] o JOIN [USER] u ON o.user_id = u.user_id JOIN ORDER_PRODUCT op ON o.order_id = op.order_id JOIN PRODUCT p ON op.product_id = p.product_id WHERE p.holder_id = ? AND o.status = 'PENDING' GROUP BY o.order_id, o.created_date, o.total_price, u.full_name, u.phone_number, u.address";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(SQL)) {
        stmt.setInt(1, sellerId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            OrderSummary order = new OrderSummary();
            order.setId(rs.getInt("id"));
            order.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
            order.setTotalPrice(rs.getBigDecimal("total_price"));
            order.setBuyerName(rs.getString("full_name"));
            order.setPhone(rs.getString("phone"));
            order.setAddress(rs.getString("address"));
            list.add(order);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

public static class OrderSummary {
    private int id;
    private LocalDateTime createdDate;
    private BigDecimal totalPrice;
    private String buyerName;
    private String phone;
    private String address;

    // Constructors
    public OrderSummary() {
    }

    public OrderSummary(int id, LocalDateTime createdDate, BigDecimal totalPrice, String buyerName, String phone, String address) {
        this.id = id;
        this.createdDate = createdDate;
        this.totalPrice = totalPrice;
        this.buyerName = buyerName;
        this.phone = phone;
        this.address = address;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}


}
