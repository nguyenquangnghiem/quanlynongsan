/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.model;

/**
 *
 * @author nghiem
 */
public class ImageOrder {
    Integer imageOrderId;
    Integer orderId;
    String urlImage;

    public ImageOrder() {
    }

    public ImageOrder(Integer imageOrderId, Integer orderId, String urlImage) {
        this.imageOrderId = imageOrderId;
        this.orderId = orderId;
        this.urlImage = urlImage;
    }

    public Integer getImageOrderId() {
        return imageOrderId;
    }

    public void setImageOrderId(Integer imageOrderId) {
        this.imageOrderId = imageOrderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
    
    
}
