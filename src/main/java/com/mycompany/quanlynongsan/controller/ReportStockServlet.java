package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.OrderRepository;
import com.mycompany.quanlynongsan.response.StockReport;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = "/secured/user/report-stock")
public class ReportStockServlet extends HttpServlet {

    private final OrderRepository orderRepository = new OrderRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        List<StockReport> reports = null;

        if (startDateStr != null && endDateStr != null && !startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = sdf.parse(startDateStr);
                Date endDate = sdf.parse(endDateStr);

                // ⚡ Lấy userId từ session để lọc đúng user hiện tại
                User user = (User) req.getSession().getAttribute("user");
                Integer userId = user.getUserId();

                reports = orderRepository.getStockReportByDateRange(userId, startDate, endDate);

            } catch (Exception ex) {
                ex.printStackTrace();
                req.setAttribute("error", "Định dạng ngày không hợp lệ!");
            }
        }

        req.setAttribute("reports", reports);
        req.getRequestDispatcher("/user/report-stock.jsp").forward(req, resp);
    }
}
