package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.ProblemRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ReportProductServlet", urlPatterns = {"/secured/user/report-product"})
public class ReportProductServlet extends HttpServlet {

    private final ProblemRepository problemRepository = new ProblemRepository();
    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId")); // đổi từ orderId sang productId
            String title = request.getParameter("name");
            String reason = request.getParameter("reason");

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            problemRepository.insert(title, reason, productId); // truyền productId vào repository

            Behavior behavior = behaviorRepository.findByCode("CREATE_PROBLEM");
            behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());

            response.sendRedirect(request.getContextPath() + "/user/home?success=report"); // đổi URL redirect
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/user/home?error=report"); // đổi URL redirect
        }
    }
}
