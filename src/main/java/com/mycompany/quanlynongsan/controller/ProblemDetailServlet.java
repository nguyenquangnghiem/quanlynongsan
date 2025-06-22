/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;

import com.mycompany.quanlynongsan.dto.ProblemDetailDTO;
import com.mycompany.quanlynongsan.repository.ProblemRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author nghiem
 */
@WebServlet("/secured/admin/problem-detail")
public class ProblemDetailServlet extends HttpServlet {
    private final ProblemRepository problemRepository = new ProblemRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int problemId = Integer.parseInt(request.getParameter("problemId"));
            ProblemDetailDTO detail = problemRepository.getProblemDetailById(problemId);
            if (detail == null) {
                response.sendRedirect(request.getContextPath() + "/secured/admin/problems?error=notfound");
                return;
            }
            request.setAttribute("detail", detail);
            request.getRequestDispatcher("/admin/problem-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/secured/admin/problems?error=invalid");
        }
    }
}
