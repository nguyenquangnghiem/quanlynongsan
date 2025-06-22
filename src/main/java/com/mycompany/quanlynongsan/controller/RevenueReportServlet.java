package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.dao.ReportDAO;
import com.mycompany.quanlynongsan.dto.RevenueReport;
import com.mycompany.quanlynongsan.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/secured/user/revenue-report")
public class RevenueReportServlet extends HttpServlet {

    private ReportDAO reportDAO = new ReportDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String type = req.getParameter("type");
        if (type == null || (!type.equals("day") && !type.equals("month") && !type.equals("year"))) {
            req.getRequestDispatcher("/user/revenue-report.jsp").forward(req, resp);
            return;
        }

        // Tính toán fromDate và toDate tự động dựa trên type
        String fromDate = null;
        String toDate = null;

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        switch (type) {
            case "day": {
                fromDate = today.withDayOfMonth(1).format(dateTimeFormatter);    // Ngày đầu tháng
                toDate = today.format(dateTimeFormatter);   
                break;// Hôm nay
            }
            case "month": {
                fromDate = today.withDayOfYear(1).format(dateTimeFormatter);     // Ngày đầu năm
                toDate = today.format(dateTimeFormatter);  
                break;// Hôm nay
            }
            case "year": {
                fromDate = null;                                             // Không giới hạn
                toDate = null;
                break;
            }
        }

        try {
            RevenueReport report = reportDAO.getRevenueReport(currentUser.getUserId(), type, fromDate, toDate);
            req.setAttribute("revenueReport", report);
            req.setAttribute("type", type);
            req.getRequestDispatcher("/user/revenue-report.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error generating report", e);
        }
    }
}
