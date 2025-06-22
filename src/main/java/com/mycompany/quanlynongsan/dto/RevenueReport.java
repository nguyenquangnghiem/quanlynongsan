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
public class RevenueReport {
    private int totalProductsSold;
    private int totalDistinctOrders;
    private BigDecimal totalRevenue;
    private List<ProductRevenueReport> products;

    public RevenueReport() {
    }

    public RevenueReport(int totalProductsSold, int totalDistinctOrders, BigDecimal totalRevenue,
            List<ProductRevenueReport> products) {
        this.totalProductsSold = totalProductsSold;
        this.totalDistinctOrders = totalDistinctOrders;
        this.totalRevenue = totalRevenue;
        this.products = products;
    }

    public int getTotalProductsSold() {
        return totalProductsSold;
    }

    public void setTotalProductsSold(int totalProductsSold) {
        this.totalProductsSold = totalProductsSold;
    }

    public int getTotalDistinctOrders() {
        return totalDistinctOrders;
    }

    public void setTotalDistinctOrders(int totalDistinctOrders) {
        this.totalDistinctOrders = totalDistinctOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<ProductRevenueReport> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRevenueReport> products) {
        this.products = products;
    }

}