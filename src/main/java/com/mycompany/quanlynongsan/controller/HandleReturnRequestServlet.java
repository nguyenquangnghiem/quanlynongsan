package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.repository.ReturnRequestRepository;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
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
            case "accept": status = "ACCEPTED"; break;
            case "reject": status = "REJECTED"; break;
            default: status = null;
        }

        String redirectUrl = req.getContextPath() + "/user/return-requests.jsp";
        if (status != null) {
            boolean success = false;
            if (status.equals("ACCEPTED")) {
                success = returnRequestRepo.acceptReturnRequest(returnRequestId, new Date());
            } else {
                success = returnRequestRepo.updateStatus(returnRequestId, status, new Date());
            }

            if (success) {
                String message = URLEncoder.encode("✅ Cập nhật trạng thái thành công!", "UTF-8");
                resp.sendRedirect(redirectUrl + "?success=" + message);
            } else {
                String message = URLEncoder.encode("❌ Cập nhật thất bại!", "UTF-8");
                resp.sendRedirect(redirectUrl + "?error=" + message);
            }
        } else {
            String message = URLEncoder.encode("❌ Thao tác không hợp lệ!", "UTF-8");
            resp.sendRedirect(redirectUrl + "?error=" + message);
        }
    }
}
