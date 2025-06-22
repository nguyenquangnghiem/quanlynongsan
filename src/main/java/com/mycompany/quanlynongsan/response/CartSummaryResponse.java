/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.response;

import java.util.List;

/**
 *
 * @author nghiem
 */
public class CartSummaryResponse {
    private int totalQuantity;
    private double totalPrice;
    private List<ItemCartResponse> itemCartResponses;

    public CartSummaryResponse(int totalQuantity, double totalPrice, List<ItemCartResponse> itemCartResponses) {
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.itemCartResponses = itemCartResponses;
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

    public List<ItemCartResponse> getItemCartResponses() {
        return itemCartResponses;
    }

    public void setItemCartResponses(List<ItemCartResponse> itemCartResponses) {
        this.itemCartResponses = itemCartResponses;
    }

}
