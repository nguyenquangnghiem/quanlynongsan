/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.model;

/**
 *
 * @author nghiem
 */
public class ImageProduct {
    Integer imageProductId;
    Integer productId;
    String urlImage;

    public ImageProduct() {
    }

    public ImageProduct(Integer imageProductId, Integer productId, String urlImage) {
        this.imageProductId = imageProductId;
        this.productId = productId;
        this.urlImage = urlImage;
    }

    public Integer getImageProductId() {
        return imageProductId;
    }

    public void setImageProductId(Integer imageProductId) {
        this.imageProductId = imageProductId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
    
    
}
