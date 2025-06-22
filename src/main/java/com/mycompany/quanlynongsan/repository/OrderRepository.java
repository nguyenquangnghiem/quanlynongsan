package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.Order;
import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.response.StockReport;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderRepository {

    private static final String FIND_DISTINCT_ORDERS_BY_PRODUCT_ID = "SELECT DISTINCT * FROM [ORDER] o JOIN ORDER_PRODUCT op ON o.order_id = op.order_id WHERE op.product_id = ? AND o.rate IS NOT NULL";

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    public OrderRepository() {
    }

    public void updateStatus(int orderId, String newStatus) {
        String sql = "UPDATE [ORDER] SET status = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Lấy danh sách đơn hàng distinct có chứa productId
    public List<Order> findDistinctOrdersByProductId(int productId) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(FIND_DISTINCT_ORDERS_BY_PRODUCT_ID)) {

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
                        rs.getTimestamp("created_date"),
                        rs.getBoolean("is_imported")
                );
                orders.add(order);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<StockReport> getStockReportByDateRange(int userId, Date startDate, Date endDate) throws Exception {
        List<StockReport> reports = new ArrayList<>();

        String sql = ""
                + "SELECT \n"
                + "    CAST(o.created_date AS DATE) AS report_date,\n"
                + "    SUM(op.quantity) AS imported_quantity,\n"
                + "    0 AS exported_quantity\n"
                + "FROM [ORDER] o\n"
                + "JOIN ORDER_PRODUCT op ON o.order_id = op.order_id\n"
                + "JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "WHERE o.is_imported = 1                     -- 🔔 Đơn hàng này là đơn nhập\n"
                + "  AND o.user_id = ?                       -- 🔔 Tôi chính là người nhập\n"
                + "  AND o.created_date BETWEEN ? AND ?\n"
                + "GROUP BY CAST(o.created_date AS DATE)\n"
                + "\n"
                + "UNION ALL\n"
                + "\n"
                + "SELECT \n"
                + "    CAST(o.created_date AS DATE) AS report_date,\n"
                + "    0 AS imported_quantity,\n"
                + "    SUM(op.quantity) AS exported_quantity\n"
                + "FROM [ORDER] o\n"
                + "JOIN ORDER_PRODUCT op ON o.order_id = op.order_id\n"
                + "JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "WHERE p.holder_id = ?                       -- 🔔 Tôi là người sở hữu sản phẩm này (bán ra)\n"
                + "  AND o.created_date BETWEEN ? AND ?\n"
                + "  AND o.status != 'PENDING'\n"
                + "GROUP BY CAST(o.created_date AS DATE)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setTimestamp(2, new Timestamp(startDate.getTime()));
            ps.setTimestamp(3, new Timestamp(endDate.getTime()));

            ps.setInt(4, userId);
            ps.setTimestamp(5, new Timestamp(startDate.getTime()));
            ps.setTimestamp(6, new Timestamp(endDate.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                Map<Date, StockReport> reportMap = new TreeMap<>();

                while (rs.next()) {
                    Date reportDate = rs.getDate("report_date");
                    int imported = rs.getInt("imported_quantity");
                    int exported = rs.getInt("exported_quantity");

                    StockReport report = reportMap.getOrDefault(reportDate, new StockReport());
                    report.setDate(reportDate);
                    report.setImportedQuantity(report.getImportedQuantity() + imported);
                    report.setExportedQuantity(report.getExportedQuantity() + exported);

                    reportMap.put(reportDate, report);
                }

                // Tính tồn kho lũy kế
                int runningStock = 0;
                for (StockReport report : reportMap.values()) {
                    runningStock += report.getImportedQuantity() - report.getExportedQuantity();
                    report.setStockRemaining(runningStock);
                    reports.add(report);
                }
            }
        }

        return reports;
    }

    public Order getById(int orderId) {
        String GET_ORDER_BY_ID_SQL = "SELECT order_id, estimated_time, comment, status, rate, payment_method, user_id, created_date, is_imported FROM [ORDER] WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(GET_ORDER_BY_ID_SQL)) {

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
                        rs.getTimestamp("created_date"),
                        rs.getBoolean("is_imported")
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null; // Không tìm thấy
    }

    public void save(Order order) {
        String INSERT_ORDER_SQL = "INSERT INTO [ORDER] (estimated_time, comment, status, rate, payment_method, user_id, created_date, is_imported) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(INSERT_ORDER_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

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
            stmt.setBoolean(8, order.getIsImported());

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

    public void updateStatus(int orderId, String status, User user) {
        String UPDATE_ORDER_STATUS_SQL = "UPDATE [ORDER] SET status = ? WHERE order_id = ?";
        String CHECK_RETURN_REQUEST_SQL = "SELECT COUNT(*) FROM RETURN_REQUEST WHERE order_id = ?";
        String UPDATE_RETURN_REQUEST_SQL = "UPDATE RETURN_REQUEST SET status = ? WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Transaction đảm bảo

            // Cập nhật trạng thái đơn hàng
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_ORDER_STATUS_SQL)) {
                stmt.setString(1, status);
                stmt.setInt(2, orderId);
                stmt.executeUpdate();
            }

            // Nếu trạng thái mới là RETURNED → kiểm tra RETURN_REQUEST
            if ("RETURNED".equalsIgnoreCase(status)) {
                boolean hasReturnRequest = false;

                // Kiểm tra có tồn tại yêu cầu hoàn trả không
                try (PreparedStatement checkStmt = conn.prepareStatement(CHECK_RETURN_REQUEST_SQL)) {
                    checkStmt.setInt(1, orderId);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            hasReturnRequest = true;
                        }
                    }
                }

                // Nếu có thì cập nhật RETURN_REQUEST.status = 'RETURNED'
                if (hasReturnRequest) {
                    try (PreparedStatement updateReturnStmt = conn.prepareStatement(UPDATE_RETURN_REQUEST_SQL)) {
                        updateReturnStmt.setString(1, "RETURNED");
                        updateReturnStmt.setInt(2, orderId);
                        updateReturnStmt.executeUpdate();
                    }
                }
            }

            // Ghi log hành vi
            Behavior behavior = behaviorRepository.findByCode("UPDATE_ORDER_STATUS");
            behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());

            conn.commit(); // Commit transaction

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cập nhật trạng thái đơn hàng thất bại: " + ex.getMessage());
        }
    }

    public void confirmOrder(int orderId) {
        String UPDATE_ORDER_STATUS_SQL = "UPDATE [ORDER] SET status = 'CONFIRMED' WHERE order_id = ? AND status = 'PENDING'";
        String GET_ORDER_PRODUCTS_SQL = "SELECT product_id, quantity FROM ORDER_PRODUCT WHERE order_id = ?";
        String GET_PRODUCT_QUANTITY_SQL = "SELECT quantity FROM PRODUCT WHERE product_id = ?";
        String UPDATE_PRODUCT_QUANTITY_SQL = "UPDATE PRODUCT SET quantity = quantity - ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // ✅ 1. Cập nhật trạng thái đơn hàng
            try (PreparedStatement updateOrderStmt = conn.prepareStatement(UPDATE_ORDER_STATUS_SQL)) {
                updateOrderStmt.setInt(1, orderId);
                int affectedRows = updateOrderStmt.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    throw new RuntimeException("Đơn hàng không tồn tại hoặc không ở trạng thái PENDING");
                }
            }

            // ✅ 2. Kiểm tra kho và trừ số lượng
            try (
                    PreparedStatement getProductsStmt = conn.prepareStatement(GET_ORDER_PRODUCTS_SQL); PreparedStatement getProductQuantityStmt = conn.prepareStatement(GET_PRODUCT_QUANTITY_SQL); PreparedStatement updateProductStmt = conn.prepareStatement(UPDATE_PRODUCT_QUANTITY_SQL)) {
                getProductsStmt.setInt(1, orderId);
                ResultSet rs = getProductsStmt.executeQuery();

                List<int[]> updateList = new ArrayList<>();
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int orderQuantity = rs.getInt("quantity");

                    getProductQuantityStmt.setInt(1, productId);
                    try (ResultSet quantityRs = getProductQuantityStmt.executeQuery()) {
                        if (quantityRs.next()) {
                            int stockQuantity = quantityRs.getInt("quantity");
                            if (stockQuantity < orderQuantity) {
                                conn.rollback();
                                throw new RuntimeException("Sản phẩm (ID: " + productId + ") không đủ số lượng trong kho. Còn: " + stockQuantity + ", cần: " + orderQuantity);
                            }
                            updateList.add(new int[]{productId, orderQuantity});
                        } else {
                            conn.rollback();
                            throw new RuntimeException("Không tìm thấy sản phẩm có ID: " + productId);
                        }
                    }
                }

                for (int[] item : updateList) {
                    updateProductStmt.setInt(1, item[1]);
                    updateProductStmt.setInt(2, item[0]);
                    updateProductStmt.addBatch();
                }
                updateProductStmt.executeBatch();
            }

            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Xác nhận đơn hàng thất bại: " + ex.getMessage());
        }
    }

    public void importProductsForUser(int orderId, int userId) {
        String GET_ORDER_PRODUCTS_SQL = "SELECT op.product_id, op.quantity, p.name, p.description, p.price, p.place_of_manufacture FROM ORDER_PRODUCT op JOIN PRODUCT p ON op.product_id = p.product_id WHERE op.order_id = ?";
        String CHECK_EXISTING_PRODUCT_SQL = "SELECT product_id FROM PRODUCT WHERE name = ? AND holder_id = ?";
        String INSERT_PRODUCT_SQL = "INSERT INTO PRODUCT (name, description, price, quantity, status, is_sell, is_browse, is_active, place_of_manufacture, holder_id, created_date) VALUES (?, ?, ?, ?, 'BINH_THUONG', 1, 1, 1, ?, ?, GETDATE())";
        String UPDATE_EXISTING_PRODUCT_QUANTITY_SQL = "UPDATE PRODUCT SET quantity = quantity + ? WHERE product_id = ?";
        String GET_CATEGORIES_OF_PRODUCT_SQL = "SELECT category_id FROM PRODUCT_CATEGORY WHERE product_id = ?";
        String INSERT_PRODUCT_CATEGORY_SQL = "INSERT INTO PRODUCT_CATEGORY (category_id, product_id) VALUES (?, ?)";
        String GET_IMAGES_OF_PRODUCT_SQL = "SELECT url_image FROM IMAGE_PRODUCT WHERE product_id = ?";
        String INSERT_IMAGE_PRODUCT_SQL = "INSERT INTO IMAGE_PRODUCT (product_id, url_image) VALUES (?, ?)";
        String UPDATE_ORDER_IMPORTED_SQL = "UPDATE [ORDER] SET is_imported = 1 WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement getProductsStmt = conn.prepareStatement(GET_ORDER_PRODUCTS_SQL); PreparedStatement checkExistingProductStmt = conn.prepareStatement(CHECK_EXISTING_PRODUCT_SQL); PreparedStatement insertProductStmt = conn.prepareStatement(INSERT_PRODUCT_SQL, Statement.RETURN_GENERATED_KEYS); PreparedStatement updateExistingProductQuantityStmt = conn.prepareStatement(UPDATE_EXISTING_PRODUCT_QUANTITY_SQL); PreparedStatement getCategoriesOfProductStmt = conn.prepareStatement(GET_CATEGORIES_OF_PRODUCT_SQL); PreparedStatement insertProductCategoryStmt = conn.prepareStatement(INSERT_PRODUCT_CATEGORY_SQL); PreparedStatement getImagesOfProductStmt = conn.prepareStatement(GET_IMAGES_OF_PRODUCT_SQL); PreparedStatement insertImageProductStmt = conn.prepareStatement(INSERT_IMAGE_PRODUCT_SQL); PreparedStatement updateOrderImportedStmt = conn.prepareStatement(UPDATE_ORDER_IMPORTED_SQL)) {

                getProductsStmt.setInt(1, orderId);
                ResultSet rs = getProductsStmt.executeQuery();

                while (rs.next()) {
                    int oldProductId = rs.getInt("product_id");
                    int orderQuantity = rs.getInt("quantity");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price");
                    String placeOfManufacture = rs.getString("place_of_manufacture");

                    checkExistingProductStmt.setString(1, name);
                    checkExistingProductStmt.setInt(2, userId);
                    try (ResultSet existingRs = checkExistingProductStmt.executeQuery()) {
                        if (existingRs.next()) {
                            // ✅ Đã có → cộng thêm số lượng
                            int existingProductId = existingRs.getInt("product_id");
                            updateExistingProductQuantityStmt.setInt(1, orderQuantity);
                            updateExistingProductQuantityStmt.setInt(2, existingProductId);
                            updateExistingProductQuantityStmt.addBatch();
                        } else {
                            // ✅ Chưa có → thêm mới sản phẩm
                            insertProductStmt.setString(1, name);
                            insertProductStmt.setString(2, description);
                            insertProductStmt.setDouble(3, price);
                            insertProductStmt.setInt(4, orderQuantity);
                            insertProductStmt.setString(5, placeOfManufacture);
                            insertProductStmt.setInt(6, userId);
                            insertProductStmt.executeUpdate();

                            try (ResultSet generatedKeys = insertProductStmt.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    int newProductId = generatedKeys.getInt(1);

                                    // ✅ Gắn category hiện tại cho sản phẩm mới
                                    getCategoriesOfProductStmt.setInt(1, oldProductId);
                                    try (ResultSet categoryRs = getCategoriesOfProductStmt.executeQuery()) {
                                        while (categoryRs.next()) {
                                            int categoryId = categoryRs.getInt("category_id");
                                            insertProductCategoryStmt.setInt(1, categoryId);
                                            insertProductCategoryStmt.setInt(2, newProductId);
                                            insertProductCategoryStmt.addBatch();
                                        }
                                    }

                                    // ✅ Gắn các ảnh hiện tại vào sản phẩm mới
                                    getImagesOfProductStmt.setInt(1, oldProductId);
                                    try (ResultSet imageRs = getImagesOfProductStmt.executeQuery()) {
                                        while (imageRs.next()) {
                                            String urlImage = imageRs.getString("url_image");
                                            insertImageProductStmt.setInt(1, newProductId);
                                            insertImageProductStmt.setString(2, urlImage);
                                            insertImageProductStmt.addBatch();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                updateExistingProductQuantityStmt.executeBatch();
                insertProductCategoryStmt.executeBatch();
                insertImageProductStmt.executeBatch();

                // ✅ Đánh dấu đơn đã nhập kho
                updateOrderImportedStmt.setInt(1, orderId);
                updateOrderImportedStmt.executeUpdate();

            }

            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Nhập kho thất bại: " + ex.getMessage());
        }
    }

    public void transferCartToOrder(int userId, int orderId, Map<Product, Integer> products) {
        String insertOrderProductSQL = "INSERT INTO ORDER_PRODUCT (order_id, product_id, quantity) VALUES (?, ?, ?)";
        String deleteCartSQL = "DELETE FROM HAS_CART WHERE user_id = ? AND product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // ✅ Transaction

            // 1️⃣ Thêm các sản phẩm vào bảng ORDER_PRODUCT
            try (PreparedStatement insertStmt = conn.prepareStatement(insertOrderProductSQL); PreparedStatement deleteStmt = conn.prepareStatement(deleteCartSQL)) {

                for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                    Product product = entry.getKey();
                    Integer quantity = entry.getValue();

                    // Thêm vào ORDER_PRODUCT
                    insertStmt.setInt(1, orderId);
                    insertStmt.setInt(2, product.getProductId());
                    insertStmt.setInt(3, quantity);
                    insertStmt.addBatch();

                    // Xóa khỏi giỏ hàng
                    deleteStmt.setInt(1, userId);
                    deleteStmt.setInt(2, product.getProductId());
                    deleteStmt.addBatch();
                }

                insertStmt.executeBatch(); // ✅ Thực thi insert
                deleteStmt.executeBatch(); // ✅ Thực thi delete
            }

            conn.commit(); // ✅ Commit transaction

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Chuyển giỏ hàng sang đơn hàng thất bại");
        }
    }

    public void updateRateAndComment(int orderId, int rate, String comment) {
        String UPDATE_SQL = "UPDATE [ORDER] SET rate = ?, comment = ?, status = ? WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

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
        final String SQL = ""
                + "SELECT o.order_id, o.created_date, u.full_name, u.phone_number, u.address\n"
                + "        FROM [ORDER] o\n"
                + "        JOIN [USER] u ON o.user_id = u.user_id\n"
                + "        JOIN ORDER_PRODUCT op ON o.order_id = op.order_id\n"
                + "        JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "        WHERE p.holder_id = ? AND o.status = 'PENDING'\n"
                + "        GROUP BY o.order_id, o.created_date, u.full_name, u.phone_number, u.address";

        final String TOTAL_PRICE_SQL = ""
                + "SELECT SUM(p.price * op.quantity) AS total_price\n"
                + "        FROM ORDER_PRODUCT op\n"
                + "        JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "        WHERE op.order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL); PreparedStatement totalPriceStmt = conn.prepareStatement(TOTAL_PRICE_SQL)) {

            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderSummary order = new OrderSummary();
                order.setId(rs.getInt("order_id"));
                order.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
                order.setBuyerName(rs.getString("full_name"));
                order.setPhone(rs.getString("phone_number"));
                order.setAddress(rs.getString("address"));

                // ✅ Tính tổng tiền cho đơn này
                totalPriceStmt.setInt(1, order.getId());
                try (ResultSet totalRs = totalPriceStmt.executeQuery()) {
                    if (totalRs.next()) {
                        order.setTotalPrice(totalRs.getBigDecimal("total_price"));
                    }
                }

                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<OrderSummary> getConfirmedOrdersBySeller(int sellerId) {
        List<OrderSummary> list = new ArrayList<>();
        final String SQL = ""
                + "SELECT o.order_id, o.created_date, u.full_name, u.phone_number, u.address\n"
                + "        FROM [ORDER] o\n"
                + "        JOIN [USER] u ON o.user_id = u.user_id\n"
                + "        JOIN ORDER_PRODUCT op ON o.order_id = op.order_id\n"
                + "        JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "        WHERE p.holder_id = ? AND (o.status = 'CONFIRMED' OR o.status = 'PAID')\n"
                + "        GROUP BY o.order_id, o.created_date, u.full_name, u.phone_number, u.address";

        final String TOTAL_PRICE_SQL = ""
                + "SELECT SUM(p.price * op.quantity) AS total_price\n"
                + "        FROM ORDER_PRODUCT op\n"
                + "        JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "        WHERE op.order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL); PreparedStatement totalPriceStmt = conn.prepareStatement(TOTAL_PRICE_SQL)) {

            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderSummary order = new OrderSummary();
                order.setId(rs.getInt("order_id"));
                order.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
                order.setBuyerName(rs.getString("full_name"));
                order.setPhone(rs.getString("phone_number"));
                order.setAddress(rs.getString("address"));

                // ✅ Tính tổng tiền cho đơn này
                totalPriceStmt.setInt(1, order.getId());
                try (ResultSet totalRs = totalPriceStmt.executeQuery()) {
                    if (totalRs.next()) {
                        order.setTotalPrice(totalRs.getBigDecimal("total_price"));
                    }
                }

                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<OrderSummary> getSuccessfulOrdersBySeller(int sellerId) {
        List<OrderSummary> list = new ArrayList<>();
        final String SQL = ""
                + "SELECT o.order_id, o.created_date, u.full_name, u.phone_number, u.address\n"
                + "        FROM [ORDER] o\n"
                + "        JOIN [USER] u ON o.user_id = u.user_id\n"
                + "        JOIN ORDER_PRODUCT op ON o.order_id = op.order_id\n"
                + "        JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "        WHERE p.holder_id = ? AND (o.status = 'SUCCESSFUL' OR o.status = 'REVIEWED')\n"
                + "        GROUP BY o.order_id, o.created_date, u.full_name, u.phone_number, u.address";

        final String TOTAL_PRICE_SQL = ""
                + "SELECT SUM(p.price * op.quantity) AS total_price\n"
                + "        FROM ORDER_PRODUCT op\n"
                + "        JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "        WHERE op.order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL); PreparedStatement totalPriceStmt = conn.prepareStatement(TOTAL_PRICE_SQL)) {

            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderSummary order = new OrderSummary();
                order.setId(rs.getInt("order_id"));
                order.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
                order.setBuyerName(rs.getString("full_name"));
                order.setPhone(rs.getString("phone_number"));
                order.setAddress(rs.getString("address"));

                // ✅ Tính tổng tiền cho đơn này
                totalPriceStmt.setInt(1, order.getId());
                try (ResultSet totalRs = totalPriceStmt.executeQuery()) {
                    if (totalRs.next()) {
                        order.setTotalPrice(totalRs.getBigDecimal("total_price"));
                    }
                }

                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<OrderSummary> getCanceledOrdersBySeller(int sellerId) {
        List<OrderSummary> list = new ArrayList<>();
        final String SQL = ""
                + "SELECT o.order_id, o.created_date, u.full_name, u.phone_number, u.address\n"
                + "        FROM [ORDER] o\n"
                + "        JOIN [USER] u ON o.user_id = u.user_id\n"
                + "        JOIN ORDER_PRODUCT op ON o.order_id = op.order_id\n"
                + "        JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "        WHERE p.holder_id = ? AND (o.status = 'CANCELED' OR o.status = 'RETURNED')\n"
                + "        GROUP BY o.order_id, o.created_date, u.full_name, u.phone_number, u.address";

        final String TOTAL_PRICE_SQL = ""
                + "SELECT SUM(p.price * op.quantity) AS total_price\n"
                + "        FROM ORDER_PRODUCT op\n"
                + "        JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "        WHERE op.order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL); PreparedStatement totalPriceStmt = conn.prepareStatement(TOTAL_PRICE_SQL)) {

            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderSummary order = new OrderSummary();
                order.setId(rs.getInt("order_id"));
                order.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
                order.setBuyerName(rs.getString("full_name"));
                order.setPhone(rs.getString("phone_number"));
                order.setAddress(rs.getString("address"));

                // ✅ Tính tổng tiền cho đơn này
                totalPriceStmt.setInt(1, order.getId());
                try (ResultSet totalRs = totalPriceStmt.executeQuery()) {
                    if (totalRs.next()) {
                        order.setTotalPrice(totalRs.getBigDecimal("total_price"));
                    }
                }

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
