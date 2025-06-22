package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.InventoryReportRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/secured/user/inventory-report")
public class InventoryReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (type == null || type.isEmpty()) {
            req.getRequestDispatcher("/user/inventory-report.jsp").forward(req, resp);
            return;
        }

        InventoryReportRepository repo = new InventoryReportRepository();
        InventoryReportRepository.InventoryReport report = repo.getInventoryReport(type, user.getUserId());

        req.setAttribute("report", report);
        req.getRequestDispatcher("/user/inventory-report.jsp").forward(req, resp);
    }
}
