/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.ImageProduct;
import com.mycompany.quanlynongsan.model.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nghiem
 */
public class ProductRepository {
    final private String FIND_10 = "SELECT TOP 10 * FROM PRODUCT p JOIN PRODUCT_CATEGORY pc ON p.product_id = pc.product_id WHERE pc.category_id = ? AND p.is_sell = 1 AND p.is_browse = 1 AND p.is_active = 1";

    final private String FIND_ALL = "SELECT * FROM PRODUCT p WHERE p.is_sell = 1 AND p.is_browse = 1 AND p.is_active = 1";
    
    final private String FIND_BY_ID = "SELECT * FROM PRODUCT p WHERE p.product_id = ? AND p.is_sell = 1 AND p.is_browse = 1 AND p.is_active = 1";
    public ProductRepository() {
    }
    
    public List<Product> find10(Integer categoryId){
        try(Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(FIND_10);
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while(rs.next()) {
                products.add(new Product(rs.getInt("product_id"), rs.getString("name"), rs.getString("description"), rs.getBigDecimal("price"), rs.getInt("quantity"), rs.getString("status"), rs.getBoolean("is_sell"), rs.getBoolean("is_browse"), rs.getString("place_of_manufacture"), rs.getBoolean("is_active"), rs.getInt("holder_id"), new Date(rs.getDate("created_date").getTime())));
            }
            return products;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return List.of();
    }
    
    public List<Product> findAll(){
        try(Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
            ResultSet rs = stmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while(rs.next()) {
                products.add(new Product(rs.getInt("product_id"), rs.getString("name"), rs.getString("description"), rs.getBigDecimal("price"), rs.getInt("quantity"), rs.getString("status"), rs.getBoolean("is_sell"), rs.getBoolean("is_browse"), rs.getString("place_of_manufacture"), rs.getBoolean("is_active"), rs.getInt("holder_id"), new Date(rs.getDate("created_date").getTime())));
            }
            return products;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return List.of();
    }
    
    public Product findById(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBigDecimal("price"),
                    rs.getInt("quantity"),
                    rs.getString("status"),
                    rs.getBoolean("is_sell"),
                    rs.getBoolean("is_browse"),
                    rs.getString("place_of_manufacture"),
                    rs.getBoolean("is_active"),
                    rs.getInt("holder_id"),
                    new Date(rs.getDate("created_date").getTime())
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public List<Product> searchProducts(String name, String[] placeOfManufacture, Double minPrice, Double maxPrice, Integer categoryId) {
    List<Product> products = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT DISTINCT p.* FROM PRODUCT p ");
    sql.append("JOIN PRODUCT_CATEGORY pc ON p.product_id = pc.product_id ");
    sql.append("WHERE p.is_sell = 1 AND p.is_browse = 1 AND p.is_active = 1");

    if (name != null && !name.trim().isEmpty()) {
        sql.append("AND p.name LIKE ? ");
    }
    if (placeOfManufacture != null && placeOfManufacture.length != 0) {
        sql.append("AND (");
        for (int i = 0; i < placeOfManufacture.length; i++) {
            sql.append("p.place_of_manufacture LIKE ?");
            if (i < placeOfManufacture.length - 1) {
                sql.append(" OR ");
            }
        }
        sql.append(") ");
    }
    if (minPrice != null) {
        sql.append("AND p.price >= ? ");
    }
    if (maxPrice != null) {
        sql.append("AND p.price <= ? ");
    }
    if (categoryId != null && categoryId.compareTo(Integer.valueOf(0)) != 0) {
        sql.append("AND pc.category_id = ? ");
    }

    try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
        int paramIndex = 1;

        if (name != null && !name.trim().isEmpty()) {
            stmt.setString(paramIndex++, "%" + name + "%");
        }
        if (placeOfManufacture != null && placeOfManufacture.length != 0) {
            stmt.setString(paramIndex++, "%" + placeOfManufacture + "%");
        }
        if (minPrice != null) {
            stmt.setDouble(paramIndex++, minPrice);
        }
        if (maxPrice != null) {
            stmt.setDouble(paramIndex++, maxPrice);
        }
        if (categoryId != null && categoryId.compareTo(Integer.valueOf(0)) != 0) {
            stmt.setInt(paramIndex++, categoryId);
        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            products.add(new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                rs.getInt("quantity"),
                rs.getString("status"),
                rs.getBoolean("is_sell"),
                rs.getBoolean("is_browse"),
                rs.getString("place_of_manufacture"),
                rs.getBoolean("is_active"),
                rs.getInt("holder_id"),
                new Date(rs.getDate("created_date").getTime())
            ));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return products;
}
private static final String FIND_BY_HOLDER_ID = "SELECT * FROM PRODUCT WHERE holder_id = ? AND is_active = 1 ORDER BY created_date DESC";

public List<Product> findByHolderId(int holderId) {
    List<Product> products = new ArrayList<>();
    
    try (Connection conn = DatabaseConnection.getConnection(); 
         PreparedStatement stmt = conn.prepareStatement(FIND_BY_HOLDER_ID)) {
         
        stmt.setInt(1, holderId);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            products.add(new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                rs.getInt("quantity"),
                rs.getString("status"),
                rs.getBoolean("is_sell"),
                rs.getBoolean("is_browse"),
                rs.getString("place_of_manufacture"),
                rs.getBoolean("is_active"),
                rs.getInt("holder_id"),
                new Date(rs.getDate("created_date").getTime())
            ));
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    
    return products;
}

public boolean add(Product product, List<String> imageProducts, Integer[] categoryIds) {
    final String INSERT_PRODUCT = "INSERT INTO PRODUCT (name, description, price, quantity, status, is_sell, is_browse, place_of_manufacture, is_active, holder_id, created_date) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    final String INSERT_IMAGE = "INSERT INTO IMAGE_PRODUCT (product_id, url_image) VALUES (?, ?)";
    final String INSERT_PRODUCT_CATEGORY = "INSERT INTO PRODUCT_CATEGORY (category_id, product_id) VALUES (?, ?)";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false); // Bắt đầu transaction

        int productId;

        // Insert PRODUCT
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_PRODUCT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setString(5, product.getStatus());
            stmt.setBoolean(6, product.getIsSell());
            stmt.setBoolean(7, product.getIsBrowse());
            stmt.setString(8, product.getPlaceOfManufacture());
            stmt.setBoolean(9, product.getIsActive());
            stmt.setInt(10, product.getHolderId());
            stmt.setTimestamp(11, new java.sql.Timestamp(product.getCreatedDate().getTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            try (var generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    productId = generatedKeys.getInt(1);
                } else {
                    conn.rollback();
                    return false;
                }
            }
        }

        // Insert IMAGE_PRODUCT
        if (imageProducts != null && !imageProducts.isEmpty()) {
            try (PreparedStatement imgStmt = conn.prepareStatement(INSERT_IMAGE)) {
                for (String img : imageProducts) {
                    imgStmt.setInt(1, productId);
                    imgStmt.setString(2, img);
                    imgStmt.addBatch();
                }
                imgStmt.executeBatch();
            }
        }

        // Insert PRODUCT_CATEGORY
        if (categoryIds != null && categoryIds.length > 0) {
            try (PreparedStatement catStmt = conn.prepareStatement(INSERT_PRODUCT_CATEGORY)) {
                for (Integer catId : categoryIds) {
                    catStmt.setInt(1, catId);
                    catStmt.setInt(2, productId);
                    catStmt.addBatch();
                }
                catStmt.executeBatch();
            }
        }

        conn.commit(); // Thành công
        return true;

    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
}

public boolean update(Product product, List<String> newImageProducts, Integer[] categoryIds) {
    final String UPDATE_PRODUCT = "UPDATE PRODUCT SET name = ?, description = ?, price = ?, quantity = ?, status = ?, is_sell = ?, is_browse = ?, place_of_manufacture = ?, is_active = ? WHERE product_id = ?";
    final String DELETE_IMAGE = "DELETE FROM IMAGE_PRODUCT WHERE product_id = ?";
    final String INSERT_IMAGE = "INSERT INTO IMAGE_PRODUCT (product_id, url_image) VALUES (?, ?)";
    final String DELETE_PRODUCT_CATEGORY = "DELETE FROM PRODUCT_CATEGORY WHERE product_id = ?";
    final String INSERT_PRODUCT_CATEGORY = "INSERT INTO PRODUCT_CATEGORY (category_id, product_id) VALUES (?, ?)";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false);

        // Update PRODUCT
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_PRODUCT)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setString(5, product.getStatus());
            stmt.setBoolean(6, product.getIsSell());
            stmt.setBoolean(7, product.getIsBrowse());
            stmt.setString(8, product.getPlaceOfManufacture());
            stmt.setBoolean(9, product.getIsActive());
            stmt.setInt(10, product.getProductId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }
        }

        // Xóa ảnh cũ
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_IMAGE)) {
            stmt.setInt(1, product.getProductId());
            stmt.executeUpdate();
        }

        // Thêm ảnh mới (nếu có)
        if (newImageProducts != null && !newImageProducts.isEmpty()) {
            try (PreparedStatement stmt = conn.prepareStatement(INSERT_IMAGE)) {
                for (String img : newImageProducts) {
                    stmt.setInt(1, product.getProductId());
                    stmt.setString(2, img);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        }

        // Xóa danh mục cũ
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_PRODUCT_CATEGORY)) {
            stmt.setInt(1, product.getProductId());
            stmt.executeUpdate();
        }

        // Thêm danh mục mới (nếu có)
        if (categoryIds != null && categoryIds.length > 0) {
            try (PreparedStatement stmt = conn.prepareStatement(INSERT_PRODUCT_CATEGORY)) {
                for (Integer catId : categoryIds) {
                    stmt.setInt(1, catId);
                    stmt.setInt(2, product.getProductId());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        }

        conn.commit();
        return true;

    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
}

public boolean deleteProduct(int productId) {
    final String DELETE_IMAGE = "DELETE FROM IMAGE_PRODUCT WHERE product_id = ?";
    final String DELETE_PRODUCT_CATEGORY = "DELETE FROM PRODUCT_CATEGORY WHERE product_id = ?";
    final String UPDATE_PRODUCT = "UPDATE PRODUCT SET is_active = 0 WHERE product_id = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false); // Bắt đầu transaction

        // Xóa ảnh
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_IMAGE)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }

        // Xóa liên kết danh mục
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_PRODUCT_CATEGORY)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }

        // Cập nhật trạng thái is_active = 0 thay cho xóa
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_PRODUCT)) {
            stmt.setInt(1, productId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false; // Không tìm thấy sản phẩm để cập nhật
            }
        }

        conn.commit(); // Thành công
        return true;

    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
}


public List<ProductSoldSummary> getSoldProductsByUserId(int holderId) {
    List<ProductSoldSummary> products = new ArrayList<>();
    String sql = " SELECT  p.product_id, p.name, p.price, p.is_sell, COALESCE(SUM(op.quantity), 0) AS sold_quantity FROM PRODUCT p LEFT JOIN ORDER_PRODUCT op ON p.product_id = op.product_id LEFT JOIN [ORDER] o ON op.order_id = o.order_id AND o.status = 'SUCCESSFUL' OR o.status = 'REVIEWED' WHERE p.holder_id = ? AND p.is_browse = 1 AND p.is_active = 1 GROUP BY p.product_id, p.name, p.price, p.is_sell";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, holderId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductSoldSummary summary = new ProductSoldSummary();
                summary.setProductId(rs.getInt("product_id"));
                summary.setName(rs.getString("name"));
                summary.setPrice(rs.getBigDecimal("price"));
                summary.setIsSell(rs.getBoolean("is_sell"));
                summary.setSoldQuantity(rs.getInt("sold_quantity"));
                products.add(summary);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return products;
}

public class ProductSoldSummary {
    private int productId;
    private String name;
    private BigDecimal price;
    private boolean isSell;
    private int soldQuantity;

        public ProductSoldSummary() {
        }
    
    

        public ProductSoldSummary(int productId, String name, BigDecimal price, boolean isSell, int soldQuantity) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.isSell = isSell;
            this.soldQuantity = soldQuantity;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public boolean isIsSell() {
            return isSell;
        }

        public void setIsSell(boolean isSell) {
            this.isSell = isSell;
        }

        public int getSoldQuantity() {
            return soldQuantity;
        }

        public void setSoldQuantity(int soldQuantity) {
            this.soldQuantity = soldQuantity;
        }

    
}


}
