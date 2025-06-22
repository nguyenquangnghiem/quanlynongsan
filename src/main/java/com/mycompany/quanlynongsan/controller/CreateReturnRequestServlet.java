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
import java.net.URLEncoder;

@WebServlet("/secured/user/return-request")
public class CreateReturnRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            String msg = URLEncoder.encode("Bạn chưa đăng nhập!", "UTF-8");
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=" + msg);
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

            String successMsg = URLEncoder.encode("Yêu cầu trả hàng đã được gửi!", "UTF-8");
            response.sendRedirect(request.getContextPath() + "/user/my-return-requests.jsp?success=" + successMsg);

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = URLEncoder.encode("❌ Gửi yêu cầu trả hàng thất bại!", "UTF-8");
            response.sendRedirect(request.getContextPath() + "/user/my-return-requests.jsp?error=" + errorMsg);
        }
    }
}
