/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.ReturnRequest;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.ReturnRequestRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/secured/user/return-request")
public class CreateReturnRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            Integer orderId = Integer.valueOf(request.getParameter("orderId"));
            String reason = request.getParameter("reason");

            ReturnRequest returnRequest = new ReturnRequest();
            returnRequest.setOrderId(orderId);
            returnRequest.setUserId(currentUser.getUserId());
            returnRequest.setReason(reason);

            ReturnRequestRepository repository = new ReturnRequestRepository();
            repository.create(returnRequest);

            response.sendRedirect(request.getContextPath() + "/user/my-return-requests.jsp?success=1");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/user/my-return-requests.jsp?error=1");
        }
    }
}
