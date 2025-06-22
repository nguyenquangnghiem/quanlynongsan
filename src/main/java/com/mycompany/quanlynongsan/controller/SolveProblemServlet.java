/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;

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

/**
 *
 * @author nghiem
 */
@WebServlet("/secured/admin/solve-problem")
public class SolveProblemServlet extends HttpServlet {
    private final ProblemRepository problemRepository = new ProblemRepository();

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/admin/problems.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            int problemId = Integer.parseInt(request.getParameter("problemId"));
            problemRepository.markAsResolved(problemId);
            Behavior behavior = behaviorRepository.findByCode("RESOLVE_PROBLEM");
            behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
            response.sendRedirect(request.getContextPath() + "/secured/admin/solve-problem?success=solved");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/secured/admin/solve-problem?error=solve");
        }
    }
}
