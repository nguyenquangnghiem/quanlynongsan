/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.quanlynongsan.dao.CategoryDAO;
import com.mycompany.quanlynongsan.model.Category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author nghiem
 */

@WebServlet(urlPatterns = { "/user/home" })
public class HomeServlet extends HttpServlet {

    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(0, "Tất cả"));
        categories.addAll(categoryDAO.findAll());

        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/user/home.jsp").forward(req, resp);
    }

}
