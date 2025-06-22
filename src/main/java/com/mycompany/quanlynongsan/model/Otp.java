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
public class Otp {
    Integer otpId;
    String email;
    String code;
    Date createdDate;

    public Otp() {
    }

    public Otp(Integer otpId, String email, String code, Date createdDate) {
        this.otpId = otpId;
        this.email = email;
        this.code = code;
        this.createdDate = createdDate;
    }

    public Integer getOtpId() {
        return otpId;
    }

    public void setOtpId(Integer otpId) {
        this.otpId = otpId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
