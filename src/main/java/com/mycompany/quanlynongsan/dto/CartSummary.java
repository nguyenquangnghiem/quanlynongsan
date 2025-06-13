/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author nghiem
 */
public class CartSummary {
    private int totalQuantity;
    private double totalPrice;
    private List<Items> items;

    public CartSummary(int totalQuantity, double totalPrice, List<Items> items) {
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    
    
    public class Items {
        private String productName;
        private Integer quantity;
        private String image;
        private Integer productId;
        private BigDecimal price;

        public Items() {
        }

        public Items(String productName, Integer quantity, String image, Integer productId, BigDecimal price) {
            this.productName = productName;
            this.quantity = quantity;
            this.image = image;
            this.productId = productId;
            this.price = price;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
        
    }
}
