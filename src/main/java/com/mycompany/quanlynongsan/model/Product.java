/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author nghiem
 */
public class Product {
    Integer productId;
    String name;
    String description;
    BigDecimal price;
    Integer quantity;
    String status;
    Boolean isSell;
    Boolean isBrowse;
    String placeOfManufacture;
    Boolean isActive;
    Integer holderId;
    Date createdDate;

    public Product() {
    }

    public Product(Integer productId, String name, String description, BigDecimal price, Integer quantity, String status, Boolean isSell, Boolean isBrowse, String placeOfManufacture, Boolean isActive, Integer holderId, Date createdDate) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.isSell = isSell;
        this.isBrowse = isBrowse;
        this.placeOfManufacture = placeOfManufacture;
        this.isActive = isActive;
        this.holderId = holderId;
        this.createdDate = createdDate;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsSell() {
        return isSell;
    }

    public void setIsSell(Boolean isSell) {
        this.isSell = isSell;
    }

    public Boolean getIsBrowse() {
        return isBrowse;
    }

    public void setIsBrowse(Boolean isBrowse) {
        this.isBrowse = isBrowse;
    }

    public String getPlaceOfManufacture() {
        return placeOfManufacture;
    }

    public void setPlaceOfManufacture(String placeOfManufacture) {
        this.placeOfManufacture = placeOfManufacture;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getHolderId() {
        return holderId;
    }

    public void setHolderId(Integer holderId) {
        this.holderId = holderId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    
    
}
