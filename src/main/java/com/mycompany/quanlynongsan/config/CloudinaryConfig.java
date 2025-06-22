/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.config;

/**
 *
 * @author nghiem
 */
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;

    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dau0dy9eu",
                    "api_key", "979983237128725",
                    "api_secret", "wprSDBGed33Dk9Dt6kyzeIQ_sEA",
                    "secure", true));
        }
        return cloudinary;
    }
}