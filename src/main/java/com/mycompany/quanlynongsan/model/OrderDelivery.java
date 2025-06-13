/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.model;

/**
 *
 * @author nghiem
 */
public class OrderDelivery {
    Integer orderId;
    Integer deliveryId;

    public OrderDelivery() {
    }

    public OrderDelivery(Integer orderId, Integer deliveryId) {
        this.orderId = orderId;
        this.deliveryId = deliveryId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }
    
    
}
