package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.net.URLEncoder;

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

@WebServlet("/secured/admin/solve-problem")
public class SolveProblemServlet extends HttpServlet {
    private final ProblemRepository problemRepository = new ProblemRepository();
    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/admin/problems.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("user");

            int problemId = Integer.parseInt(request.getParameter("problemId"));
            problemRepository.markAsResolved(problemId);

            Behavior behavior = behaviorRepository.findByCode("RESOLVE_PROBLEM");
            behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());

            String successMsg = "Đã đánh dấu vấn đề là đã giải quyết.";
            response.sendRedirect(request.getContextPath() + "/admin/problems.jsp?success=" + URLEncoder.encode(successMsg, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = "Đã xảy ra lỗi khi xử lý vấn đề.";
            response.sendRedirect(request.getContextPath() + "/admin/problems.jsp?error=" + URLEncoder.encode(errorMsg, "UTF-8"));
        }
    }
}
