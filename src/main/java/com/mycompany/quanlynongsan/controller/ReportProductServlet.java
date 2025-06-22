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
import java.net.URLEncoder;

@WebServlet(name = "ReportProductServlet", urlPatterns = {"/user/report-product"})
public class ReportProductServlet extends HttpServlet {

    private final ProblemRepository problemRepository = new ProblemRepository();
    private final BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String successMessage = "";
        String errorMessage = "";

        try {
            int productId = Integer.parseInt(request.getParameter("productId")); // lấy productId
            String title = request.getParameter("name");
            String reason = request.getParameter("reason");

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            problemRepository.insert(title, reason, productId); // thêm vào DB
            if(user != null) {
                Behavior behavior = behaviorRepository.findByCode("CREATE_PROBLEM");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
            }

            successMessage = "Báo cáo sản phẩm thành công!";
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Đã xảy ra lỗi khi gửi báo cáo.";
        }

        String redirectUrl = request.getContextPath() + "/user/home";

        if (!successMessage.isEmpty()) {
            redirectUrl += "?success=" + URLEncoder.encode(successMessage, "UTF-8");
        } else if (!errorMessage.isEmpty()) {
            redirectUrl += "?error=" + URLEncoder.encode(errorMessage, "UTF-8");
        }

        response.sendRedirect(redirectUrl);
    }
}
