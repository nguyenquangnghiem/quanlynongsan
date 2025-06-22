package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.repository.InventoryReportRepository;
import com.mycompany.quanlynongsan.response.SystemRevenueReport;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/secured/admin/system-revenue-report")
public class SystemRevenueReportServlet extends HttpServlet {

    private InventoryReportRepository reportRepository = new InventoryReportRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy tham số
        String type = req.getParameter("type");        // "day", "month", "year"
        String fromDateStr = req.getParameter("fromDate");  // format "yyyy-MM-dd"
        String toDateStr = req.getParameter("toDate");      // format "yyyy-MM-dd"

        // Đưa lại vào request để giữ lựa chọn form
        req.setAttribute("type", type);
        req.setAttribute("fromDate", fromDateStr);
        req.setAttribute("toDate", toDateStr);

        List<SystemRevenueReport> reportList = new ArrayList<>();
        BigDecimal totalFarmer = BigDecimal.ZERO;
        BigDecimal totalImportCost = BigDecimal.ZERO;
        BigDecimal totalDistributor = BigDecimal.ZERO;
        String errorMsg = null;

        if (type != null && !type.isBlank() && fromDateStr != null && !fromDateStr.isBlank()
                && toDateStr != null && !toDateStr.isBlank()) {
            try {
                LocalDate fromDate = LocalDate.parse(fromDateStr);
                LocalDate toDate = LocalDate.parse(toDateStr);
                // optional: kiểm tra fromDate <= toDate
                if (fromDate.isAfter(toDate)) {
                    errorMsg = "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc.";
                } else {
                    // Gọi repository lấy báo cáo
                    reportList = reportRepository.getSystemRevenueReport(type, fromDate, toDate);

                    // Tính tổng
                    for (SystemRevenueReport r : reportList) {
                        if (r.getFarmerRevenue() != null) {
                            totalFarmer = totalFarmer.add(r.getFarmerRevenue());
                        }
                        if (r.getDistributorImportCost() != null) {
                            totalImportCost = totalImportCost.add(r.getDistributorImportCost());
                        }
                        if (r.getDistributorRevenue() != null) {
                            totalDistributor = totalDistributor.add(r.getDistributorRevenue());
                        }
                    }
                }
            } catch (DateTimeParseException e) {
                errorMsg = "Ngày không hợp lệ. Vui lòng nhập theo định dạng yyyy-MM-dd.";
            } catch (SQLException e) {
                errorMsg = "Lỗi khi truy vấn dữ liệu: " + e.getMessage();
                e.printStackTrace();
            }
        }
        // Đưa kết quả vào request
        req.setAttribute("reportList", reportList);
        req.setAttribute("totalFarmerRevenue", totalFarmer);
        req.setAttribute("totalImportCost", totalImportCost);
        req.setAttribute("totalDistributorRevenue", totalDistributor);
        req.setAttribute("errorMsg", errorMsg);

        // Forward đến JSP
        req.getRequestDispatcher("/admin/system-revenue-report.jsp").forward(req, resp);
    }
}
