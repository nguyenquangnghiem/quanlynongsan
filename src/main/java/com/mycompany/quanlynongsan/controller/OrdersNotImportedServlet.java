/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.util.List;

import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.OrderProductRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author nghiem
 */

@WebServlet(urlPatterns = "/secured/user/orders-not-imported")
public class OrdersNotImportedServlet extends HttpServlet {

    private OrderProductRepository orderProductRepository = new OrderProductRepository();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = ((User) request.getSession().getAttribute("user")).getUserId();

        List<OrderProductRepository.OrderSummary> orders = orderProductRepository.findConfirmedOrdersNotInStock(userId);

        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/user/orders-not-imported.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp); // Generated from
                                 // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

}
