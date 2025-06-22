/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.dto.ProductRevenueReport;
import com.mycompany.quanlynongsan.dto.RevenueReport;

/**
 *
 * @author nghiem
 */
public class ReportDAO {

    public ReportDAO() {
    }

    // Báo cáo tổng quan
    public RevenueReport getRevenueReport(int sellerId, String type, String fromDate, String toDate)
            throws SQLException {
        RevenueReport report = new RevenueReport();

        String sqlTotal = ""
                + "SELECT \n"
                + "                SUM(od.quantity) AS total_products_sold,\n"
                + "                COUNT(DISTINCT o.order_id) AS total_orders,\n"
                + "                SUM(od.quantity * p.price) AS total_revenue\n"
                + "            FROM [ORDER] o\n"
                + "            JOIN [ORDER_PRODUCT] od ON o.order_id = od.order_id\n"
                + "            JOIN [PRODUCT] p ON od.product_id = p.product_id\n"
                + "            WHERE p.holder_id = ?\n"
                + "              AND (o.status = 'SUCCESSFUL' OR o.status = 'REVIEWED')\n"
                + "              AND (o.created_date >= ? OR ? IS NULL)\n"
                + "              AND (o.created_date <= ? OR ? IS NULL)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlTotal)) {

            ps.setInt(1, sellerId);
            ps.setString(2, fromDate);
            ps.setString(3, fromDate);
            ps.setString(4, toDate);
            ps.setString(5, toDate);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    report.setTotalProductsSold(rs.getInt("total_products_sold"));
                    report.setTotalDistinctOrders(rs.getInt("total_orders"));
                    report.setTotalRevenue(rs.getBigDecimal("total_revenue"));
                }
            }
        }

        report.setProducts(getProductRevenueReport(sellerId, type, fromDate, toDate));
        return report;
    }

    // Báo cáo theo từng sản phẩm + theo loại (ngày/tháng/năm)
    public List<ProductRevenueReport> getProductRevenueReport(int sellerId, String type, String fromDate, String toDate)
            throws SQLException {
        List<ProductRevenueReport> list = new ArrayList<>();

        String groupField;
        String selectTimeField;

        switch (type) {
            case "day": {
                selectTimeField = "CONVERT(DATE, o.created_date) AS time_group";
                groupField = "CONVERT(DATE, o.created_date)";
                break;
            }
            case "month": {
                selectTimeField = "FORMAT(o.created_date, 'yyyy-MM') AS time_group";
                groupField = "FORMAT(o.created_date, 'yyyy-MM')";
                break;
            }
            case "year": {
                selectTimeField = "YEAR(o.created_date) AS time_group";
                groupField = "YEAR(o.created_date)";
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid report type");
            }
        }

        String sql = String.format(""
                + "SELECT \n"
                + "                p.product_id AS product_id,\n"
                + "                p.name AS product_name,\n"
                + "                p.price AS product_price,\n"
                + "                %s,\n"
                + "                SUM(od.quantity) AS total_quantity_sold,\n"
                + "                COUNT(DISTINCT o.order_id) AS total_distinct_orders,\n"
                + "                SUM(od.quantity * p.price) AS total_revenue\n"
                + "            FROM [ORDER] o\n"
                + "            JOIN [ORDER_PRODUCT] od ON o.order_id = od.order_id\n"
                + "            JOIN [PRODUCT] p ON od.product_id = p.product_id\n"
                + "            WHERE p.holder_id = ?\n"
                + "              AND (o.status = 'SUCCESSFUL' OR o.status = 'REVIEWED')\n"
                + "              AND (o.created_date >= ? OR ? IS NULL)\n"
                + "              AND (o.created_date <= ? OR ? IS NULL)\n"
                + "            GROUP BY p.product_id, p.name, p.price, %s\n"
                + "            ORDER BY %s DESC", selectTimeField, groupField, groupField);

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            ps.setString(2, fromDate);
            ps.setString(3, fromDate);
            ps.setString(4, toDate);
            ps.setString(5, toDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductRevenueReport item = new ProductRevenueReport();
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductPrice(rs.getBigDecimal("product_price"));
                    item.setTotalQuantitySold(rs.getInt("total_quantity_sold"));
                    item.setTotalDistinctOrders(rs.getInt("total_distinct_orders"));
                    item.setTotalRevenue(rs.getBigDecimal("total_revenue"));

                    switch (type) {
                        case "day": {
                            item.setDateDisplay(rs.getString("time_group"));
                            break;
                        }
                        case "month": {
                            item.setMonthDisplay(rs.getString("time_group"));
                            break;
                        }
                        case "year": {
                            item.setYear(rs.getInt("time_group"));
                            break;
                        }
                    }

                    list.add(item);
                }
            }
        }

        return list;
    }
}
