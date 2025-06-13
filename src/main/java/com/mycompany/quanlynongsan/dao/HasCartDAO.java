/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.dao;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.dto.CartSummary;
import com.mycompany.quanlynongsan.repository.HasCartRepository;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author nghiem
 */
public class HasCartDAO {

    private HasCartRepository hasCartRepository = new HasCartRepository();

    public HasCartDAO() {
    }

    // ✅ 1. Hàm thêm sản phẩm vào giỏ hàng (nếu có rồi thì tăng số lượng)
    public void addProductToCart(Integer userId, Integer productId, Integer quantity) {
        hasCartRepository.addToCart(userId, productId, quantity);
    }
    
    public CartSummary getCartSummary(Integer userId) {
        return hasCartRepository.getCartSummary(userId);
    }
    
    public void removeProductFromCart(Integer userId, Integer productId) {
    hasCartRepository.removeProductFromCart(userId, productId);
}
}
