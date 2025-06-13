/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.quanlynongsan.dao.HasCartDAO;
import com.mycompany.quanlynongsan.dto.CartSummary;
import com.mycompany.quanlynongsan.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author nghiem
 */
@WebServlet(urlPatterns = "/secured/user/has-cart")
public class HasCartController extends HttpServlet {

    private HasCartDAO hasCartDAO = new HasCartDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        Integer productId = Integer.valueOf(req.getParameter("productId"));
        Integer quantity = Integer.valueOf(req.getParameter("quantity")); // số lượng muốn thêm

        resp.setContentType("application/json");

        try {
            hasCartDAO.addProductToCart(user.getUserId(), productId, quantity);
            new ObjectMapper().writeValue(resp.getWriter(), true); // thành công
        } catch (Exception e) {
            e.printStackTrace();
            new ObjectMapper().writeValue(resp.getWriter(), false); // thất bại
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            CartSummary summary = hasCartDAO.getCartSummary(user.getUserId());
            new ObjectMapper().writeValue(resp.getWriter(), summary);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new ObjectMapper().writeValue(resp.getWriter(), "Lỗi lấy giỏ hàng");
        }
    }

    
}
