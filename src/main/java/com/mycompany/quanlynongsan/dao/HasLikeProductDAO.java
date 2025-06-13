/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.dao;

import com.mycompany.quanlynongsan.repository.HasLikeProductRepository;

/**
 *
 * @author nghiem
 */
public class HasLikeProductDAO {
    private HasLikeProductRepository hasLikeProductRepository = new HasLikeProductRepository();

    public HasLikeProductDAO() {
    }

    // ✅ 1. Hàm kiểm tra người dùng có yêu thích sản phẩm hay không
    public Boolean checkLikeProduct(Integer userId, Integer productId) {
        return hasLikeProductRepository.isProductFavorited(userId, productId);
    }

    // ✅ 2. Hàm thêm sản phẩm yêu thích
    public Boolean addLikeProduct(Integer userId, Integer productId) {
        return hasLikeProductRepository.addFavoriteProduct(userId, productId);
    }

    // ✅ 3. Hàm xóa sản phẩm yêu thích
    public Boolean removeLikeProduct(Integer userId, Integer productId) {
        return hasLikeProductRepository.removeFavoriteProduct(userId, productId);
    }
}
