/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

/**
 *
 * @author nghiem
 */
import java.io.IOException;
import java.util.List;

import com.mycompany.quanlynongsan.repository.BehaviorLogRepository;
import com.mycompany.quanlynongsan.response.BehaviorLog;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/secured/admin/behavior-logs")
public class BehaviorLogServlet extends HttpServlet {

    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1)
                    page = 1;
            } catch (NumberFormatException ignored) {
            }
        }

        BehaviorLogRepository repo = new BehaviorLogRepository();
        List<BehaviorLog> logs = repo.findBehaviorLogs(page, PAGE_SIZE);
        int totalRecords = repo.countTotalBehaviorLogs();
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        request.setAttribute("logs", logs);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/admin/behavior-logs.jsp").forward(request, response);
    }
}
