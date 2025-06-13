/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.quanlynongsan.dao.HasLikeProductDAO;
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
@WebServlet(urlPatterns = "/secured/user/has-like-product")
public class HasLikeProductController extends HttpServlet {

    private HasLikeProductDAO hasLikeProductDAO = new HasLikeProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        Integer productId = Integer.valueOf(req.getParameter("productId"));
        resp.setContentType("application/json");
        Boolean isHasLikeProduct = hasLikeProductDAO.checkLikeProduct(user.getUserId(), productId);
        new ObjectMapper().writeValue(resp.getWriter(), isHasLikeProduct);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        Integer productId = Integer.valueOf(req.getParameter("productId"));
        resp.setContentType("application/json");
        boolean success = hasLikeProductDAO.addLikeProduct(user.getUserId(), productId);
        new ObjectMapper().writeValue(resp.getWriter(), success);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        Integer productId = Integer.valueOf(req.getParameter("productId"));
        resp.setContentType("application/json");
        boolean success = hasLikeProductDAO.removeLikeProduct(user.getUserId(), productId);
        new ObjectMapper().writeValue(resp.getWriter(), success);
    }
}
