/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.dao;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.mycompany.quanlynongsan.model.HasCart;
import com.mycompany.quanlynongsan.model.ImageProduct;
import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.repository.HasCartRepository;
import com.mycompany.quanlynongsan.repository.ImageProductRepository;
import com.mycompany.quanlynongsan.repository.ProductRepository;
import com.mycompany.quanlynongsan.response.CartSummaryResponse;
import com.mycompany.quanlynongsan.response.ItemCartResponse;

/**
 *
 * @author nghiem
 */
public class HasCartDAO {

    private HasCartRepository hasCartRepository = new HasCartRepository();

    private ProductRepository productRepository = new ProductRepository();

    private ImageProductRepository imageProductRepository = new ImageProductRepository();

    public HasCartDAO() {
    }

    // ✅ 1. Hàm thêm sản phẩm vào giỏ hàng (nếu có rồi thì tăng số lượng)
    public void addProductToCart(Integer userId, Integer productId, Integer quantity) {
        hasCartRepository.addToCart(userId, productId, quantity);
    }

    public CartSummaryResponse getCartSummary(Integer userId) {
        List<HasCart> hasCarts = hasCartRepository.getHasCartByUserId(userId);
        AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);
        List<ItemCartResponse> itemCartResponses = hasCarts.stream().map(hasCart -> {
            Product product = productRepository.findById(hasCart.getProductId());
            List<ImageProduct> imageProducts = imageProductRepository.findByProductId(product.getProductId());
            totalPrice.updateAndGet(v -> v + product.getPrice().doubleValue() * hasCart.getQuantity());
            return new ItemCartResponse(product.getName(), hasCart.getQuantity(),
                    (imageProducts.isEmpty() ? "" : imageProducts.get(0).getUrlImage()),
                    product.getProductId(), product.getPrice());
        }).collect(Collectors.toList());

        return new CartSummaryResponse(itemCartResponses.size(), totalPrice.get(), itemCartResponses);

    }

    public void removeProductFromCart(Integer userId, Integer productId) {
        hasCartRepository.removeProductFromCart(userId, productId);
    }
}
