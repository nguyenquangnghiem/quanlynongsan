/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.quanlynongsan.dao.HasCartDAO;
import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.response.CartSummaryResponse;

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
@WebServlet(urlPatterns = "/secured/user/has-cart")
public class HasCartServlet extends HttpServlet {

    private HasCartDAO hasCartDAO = new HasCartDAO();

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        Integer productId = Integer.valueOf(req.getParameter("productId"));
        Integer quantity = Integer.valueOf(req.getParameter("quantity")); // số lượng muốn thêm

        resp.setContentType("application/json");

        try {
            hasCartDAO.addProductToCart(user.getUserId(), productId, quantity);
            Behavior behavior = behaviorRepository.findByCode("ADD_TO_CART");
            behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
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
            CartSummaryResponse summary = hasCartDAO.getCartSummary(user.getUserId());
            new ObjectMapper().writeValue(resp.getWriter(), summary);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new ObjectMapper().writeValue(resp.getWriter(), "Lỗi lấy giỏ hàng");
        }
    }

}
