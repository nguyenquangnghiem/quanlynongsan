/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.model;

import java.util.Date;

/**
 *
 * @author nghiem
 */
public class Delivery {
    Integer deliveryId;
    String address;
    Date createdDate;

    public Delivery() {
    }

    public Delivery(Integer deliveryId, String address, Date createdDate) {
        this.deliveryId = deliveryId;
        this.address = address;
        this.createdDate = createdDate;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    
}
