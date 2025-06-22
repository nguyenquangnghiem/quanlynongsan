/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.dto;

import java.math.BigDecimal;

/**
 *
 * @author nghiem
 */
public class ProductRevenueReport {
    private Integer productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer totalQuantitySold;
    private Integer totalDistinctOrders;
    private BigDecimal totalRevenue;
    private String dateDisplay; // Theo ngày
    private String monthDisplay; // Theo tháng/năm
    private int year;

    public ProductRevenueReport() {
    }

    public ProductRevenueReport(Integer productId, String productName, BigDecimal productPrice,
            Integer totalQuantitySold, Integer totalDistinctOrders, BigDecimal totalRevenue, String dateDisplay,
            String monthDisplay, int year) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuantitySold = totalQuantitySold;
        this.totalDistinctOrders = totalDistinctOrders;
        this.totalRevenue = totalRevenue;
        this.dateDisplay = dateDisplay;
        this.monthDisplay = monthDisplay;
        this.year = year;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public void setTotalQuantitySold(Integer totalQuantitySold) {
        this.totalQuantitySold = totalQuantitySold;
    }

    public Integer getTotalDistinctOrders() {
        return totalDistinctOrders;
    }

    public void setTotalDistinctOrders(Integer totalDistinctOrders) {
        this.totalDistinctOrders = totalDistinctOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getDateDisplay() {
        return dateDisplay;
    }

    public void setDateDisplay(String dateDisplay) {
        this.dateDisplay = dateDisplay;
    }

    public String getMonthDisplay() {
        return monthDisplay;
    }

    public void setMonthDisplay(String monthDisplay) {
        this.monthDisplay = monthDisplay;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}