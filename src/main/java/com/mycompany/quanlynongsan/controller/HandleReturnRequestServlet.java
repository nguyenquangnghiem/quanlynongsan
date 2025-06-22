/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.repository.ReturnRequestRepository;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Date;

@WebServlet("/secured/seller/handle-return-request")
public class HandleReturnRequestServlet extends HttpServlet {

    private final ReturnRequestRepository returnRequestRepo = new ReturnRequestRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action"); // accept or reject
        Integer returnRequestId = Integer.parseInt(req.getParameter("returnRequestId"));

        String status;
        switch (action) {
            case "accept": status = "ACCEPTED";break;
            case "reject": status = "REJECTED";break;
            default: status = null;
        };

        if (status != null) {
            boolean success = false;
            if(status.equals("ACCEPTED")) {
                success = returnRequestRepo.acceptReturnRequest(returnRequestId, new Date());
            } else success = returnRequestRepo.updateStatus(returnRequestId, status, new Date());
            if (success) {
                req.getSession().setAttribute("message", "Cập nhật trạng thái thành công!");
            } else {
                req.getSession().setAttribute("error", "Cập nhật thất bại.");
            }
        }

        resp.sendRedirect(req.getContextPath() + "/user/return-requests.jsp");
    }
}
