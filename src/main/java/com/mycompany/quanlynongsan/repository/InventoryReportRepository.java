package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.response.SystemRevenueReport;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class InventoryReportRepository {

    public static class ReportGroup {

        private String dateDisplay;
        private String monthDisplay;
        private String year;

        private String productName;
        private double importPrice; // ✅ Giá nhập (holder_id = 1)
        private int totalImported;
        private double totalImportCost;

        private double sellPrice; // ✅ Giá bán (holder_id = 3)
        private int totalSold;
        private double totalRevenue;

        // Getter & Setter
        public String getDateDisplay() {
            return dateDisplay;
        }

        public void setDateDisplay(String dateDisplay) {
            this.dateDisplay = dateDisplay;
        }

        public String getMonthDisplay() {
            return monthDisplay;
        }

        public void setMonthDisplay(String monthDisplay) {
            this.monthDisplay = monthDisplay;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public double getImportPrice() {
            return importPrice;
        }

        public void setImportPrice(double importPrice) {
            this.importPrice = importPrice;
        }

        public int getTotalImported() {
            return totalImported;
        }

        public void setTotalImported(int totalImported) {
            this.totalImported = totalImported;
        }

        public double getTotalImportCost() {
            return totalImportCost;
        }

        public void setTotalImportCost(double totalImportCost) {
            this.totalImportCost = totalImportCost;
        }

        public double getSellPrice() {
            return sellPrice;
        }

        public void setSellPrice(double sellPrice) {
            this.sellPrice = sellPrice;
        }

        public int getTotalSold() {
            return totalSold;
        }

        public void setTotalSold(int totalSold) {
            this.totalSold = totalSold;
        }

        public double getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(double totalRevenue) {
            this.totalRevenue = totalRevenue;
        }
    }

    public static class InventoryReport {

        private int totalImportedProducts;
        private double totalImportCost;

        private int totalSoldProducts;
        private double totalSalesRevenue;

        private int totalImportEvents;
        private int totalSoldOrders;

        private List<ReportGroup> details;

        // Getter & Setter
        public int getTotalImportedProducts() {
            return totalImportedProducts;
        }

        public void setTotalImportedProducts(int totalImportedProducts) {
            this.totalImportedProducts = totalImportedProducts;
        }

        public double getTotalImportCost() {
            return totalImportCost;
        }

        public void setTotalImportCost(double totalImportCost) {
            this.totalImportCost = totalImportCost;
        }

        public int getTotalSoldProducts() {
            return totalSoldProducts;
        }

        public void setTotalSoldProducts(int totalSoldProducts) {
            this.totalSoldProducts = totalSoldProducts;
        }

        public double getTotalSalesRevenue() {
            return totalSalesRevenue;
        }

        public void setTotalSalesRevenue(double totalSalesRevenue) {
            this.totalSalesRevenue = totalSalesRevenue;
        }

        public int getTotalImportEvents() {
            return totalImportEvents;
        }

        public void setTotalImportEvents(int totalImportEvents) {
            this.totalImportEvents = totalImportEvents;
        }

        public int getTotalSoldOrders() {
            return totalSoldOrders;
        }

        public void setTotalSoldOrders(int totalSoldOrders) {
            this.totalSoldOrders = totalSoldOrders;
        }

        public List<ReportGroup> getDetails() {
            return details;
        }

        public void setDetails(List<ReportGroup> details) {
            this.details = details;
        }
    }

    public InventoryReport getInventoryReport(String type, Integer userId) {
        InventoryReport report = new InventoryReport();
        List<ReportGroup> list = new ArrayList<>();

        String timeFormatSQL;
        switch (type) {
            case "day":
                timeFormatSQL = "CONVERT(VARCHAR(10), o.created_date, 23)";
                break;
            case "month":
                timeFormatSQL = "FORMAT(o.created_date, 'yyyy-MM')";
                break;
            case "year":
                timeFormatSQL = "FORMAT(o.created_date, 'yyyy')";
                break;
            default:
                return report;
        }

        String sql = String.format(""
                + "SELECT\n"
                + "    %s AS time_group,\n"
                + "    p.name AS product_name,\n"
                + "\n"
                + "    -- DỮ LIỆU NHẬP KHO (lấy 1 giá nhập dựa vào product name)\n"
                + "    (SELECT TOP 1 p1.price\n"
                + "     FROM PRODUCT p1\n"
                + "     JOIN [USER] u1 ON p1.holder_id = u1.user_id\n"
                + "     WHERE p1.name = p.name AND u1.role_id = 1\n"
                + "    ) AS import_price,\n"
                + "\n"
                + "    SUM(CASE WHEN u.role_id = 1 THEN op.quantity ELSE 0 END) AS total_imported,\n"
                + "    SUM(CASE WHEN u.role_id = 1 THEN op.quantity * p.price ELSE 0 END) AS total_import_cost,\n"
                + "\n"
                + "    -- DỮ LIỆU BÁN RA (lấy 1 giá bán dựa vào product name)\n"
                + "    (SELECT TOP 1 p2.price\n"
                + "     FROM PRODUCT p2\n"
                + "     JOIN [USER] u2 ON p2.holder_id = u2.user_id\n"
                + "     WHERE p2.name = p.name AND u2.role_id = 2\n"
                + "    ) AS sell_price,\n"
                + "\n"
                + "    SUM(CASE WHEN u.role_id = 2 THEN op.quantity ELSE 0 END) AS total_sold,\n"
                + "    SUM(CASE WHEN u.role_id = 2 THEN op.quantity * p.price ELSE 0 END) AS total_revenue\n"
                + "\n"
                + "FROM [ORDER] o\n"
                + "JOIN [USER] buyer ON o.user_id = buyer.user_id -- Người thực hiện đơn hàng (có thể là bạn hoặc khách hàng)\n"
                + "JOIN ORDER_PRODUCT op ON o.order_id = op.order_id\n"
                + "JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "JOIN [USER] u ON p.holder_id = u.user_id -- Người sở hữu sản phẩm\n"
                + "\n"
                + "WHERE o.status IN ('PAID', 'SUCCESSFUL', 'REVIEWED')\n"
                + "AND (o.user_id = ? OR buyer.role_id = 3)\n"
                + "\n"
                + "GROUP BY %s, p.name\n"
                + "ORDER BY time_group DESC", timeFormatSQL, timeFormatSQL);

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReportGroup g = new ReportGroup();
                g.setDateDisplay(rs.getString("time_group"));
                g.setMonthDisplay(rs.getString("time_group"));
                g.setYear(rs.getString("time_group"));
                g.setProductName(rs.getString("product_name"));
                g.setImportPrice(rs.getDouble("import_price"));
                g.setTotalImported(rs.getInt("total_imported"));
                g.setTotalImportCost(rs.getDouble("total_import_cost"));
                g.setSellPrice(rs.getDouble("sell_price"));
                g.setTotalSold(rs.getInt("total_sold"));
                g.setTotalRevenue(rs.getDouble("total_revenue"));

                list.add(g);

                report.setTotalImportedProducts(report.getTotalImportedProducts() + g.getTotalImported());
                report.setTotalImportCost(report.getTotalImportCost() + g.getTotalImportCost());
                report.setTotalSoldProducts(report.getTotalSoldProducts() + g.getTotalSold());
                report.setTotalSalesRevenue(report.getTotalSalesRevenue() + g.getTotalRevenue());
            }

            report.setDetails(list);

            // Tổng số lần nhập hàng
            String sqlCountImport = ""
                    + "SELECT COUNT(DISTINCT o.order_id) AS totalImportEvents\n"
                    + "            FROM [ORDER] o\n"
                    + "            JOIN ORDER_PRODUCT op ON o.order_id = op.order_id\n"
                    + "            JOIN PRODUCT p ON op.product_id = p.product_id\n"
                    + "            WHERE p.holder_id = 1 AND o.status IN ('PAID', 'SUCCESSFUL', 'REVIEWED')";

            try (PreparedStatement psCount = conn.prepareStatement(sqlCountImport); ResultSet rsCount = psCount.executeQuery()) {
                if (rsCount.next()) {
                    report.setTotalImportEvents(rsCount.getInt("totalImportEvents"));
                }
            }

            // Tổng số đơn bán thành công
            String sqlCountSold = ""
                    + "SELECT COUNT(DISTINCT o.order_id) AS totalSoldOrders\n"
                    + "            FROM [ORDER] o\n"
                    + "            JOIN ORDER_PRODUCT op ON o.order_id = op.order_id\n"
                    + "            JOIN PRODUCT p ON op.product_id = p.product_id\n"
                    + "            WHERE p.holder_id = 3 AND o.status IN ('PAID', 'SUCCESSFUL', 'REVIEWED')";

            try (PreparedStatement psCountSold = conn.prepareStatement(sqlCountSold); ResultSet rsCountSold = psCountSold.executeQuery()) {
                if (rsCountSold.next()) {
                    report.setTotalSoldOrders(rsCountSold.getInt("totalSoldOrders"));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return report;
    }

    public List<SystemRevenueReport> getSystemRevenueReport(
            String type, LocalDate fromDate, LocalDate toDate) throws SQLException {

        List<SystemRevenueReport> result = new ArrayList<>();

        // Xây timeFormatSQL
        String timeFormatSQL;
        switch (type) {
            case "day":
                timeFormatSQL = "CONVERT(VARCHAR(10), o.created_date, 23)";
                break;
            case "month":
                timeFormatSQL = "FORMAT(o.created_date, 'yyyy-MM')";
                break;
            case "year":
                timeFormatSQL = "FORMAT(o.created_date, 'yyyy')";
                break;
            default:
                return result;
        }

        String sql = String.format(
                "SELECT %s AS time_group,\n"
                + "       SUM(CASE WHEN p_holder.role_id = 1 AND buyer.role_id = 2 THEN op.quantity * p.price ELSE 0 END) AS farmer_revenue,\n"
                + "       SUM(CASE WHEN p_holder.role_id = 1 AND buyer.role_id = 2 THEN op.quantity * p.price ELSE 0 END) AS distributor_import_cost,\n"
                + "       SUM(CASE WHEN p_holder.role_id = 2 AND buyer.role_id = 3 THEN op.quantity * p.price ELSE 0 END) AS distributor_revenue\n"
                + "FROM [ORDER] o\n"
                + "JOIN ORDER_PRODUCT op ON o.order_id = op.order_id\n"
                + "JOIN PRODUCT p ON op.product_id = p.product_id\n"
                + "JOIN [USER] buyer ON o.user_id = buyer.user_id\n"
                + "JOIN [USER] p_holder ON p.holder_id = p_holder.user_id\n"
                + "WHERE o.created_date >= ? AND o.created_date <= ?\n"
                + "  AND o.status IN ('SUCCESSFUL','REVIEWED')\n"
                + "GROUP BY %s\n"
                + "ORDER BY time_group DESC",
                timeFormatSQL, timeFormatSQL
        );

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Truyền tham số ngày
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(fromDate.atStartOfDay()));
            // Với toDate, để bao gồm cả ngày toDate, ta có thể set đến cuối ngày:
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(toDate.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SystemRevenueReport r = new SystemRevenueReport();
                    r.setTimeGroup(rs.getString("time_group"));
                    r.setFarmerRevenue(rs.getBigDecimal("farmer_revenue"));
                    r.setDistributorImportCost(rs.getBigDecimal("distributor_import_cost"));
                    r.setDistributorRevenue(rs.getBigDecimal("distributor_revenue"));
                    result.add(r);
                }
            }
        }
        return result;
    }

}
