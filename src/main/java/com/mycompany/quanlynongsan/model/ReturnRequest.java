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
public class ReturnRequest {
    private Integer returnRequestId;
    private Integer orderId;
    private Integer userId;
    private String reason;
    private String status;
    private Date createdDate;
    private Date resolvedDate;

    public ReturnRequest() {
    }

    public ReturnRequest(Integer returnRequestId, Integer orderId, Integer userId, String reason, String status,
            Date createdDate, Date resolvedDate) {
        this.returnRequestId = returnRequestId;
        this.orderId = orderId;
        this.userId = userId;
        this.reason = reason;
        this.status = status;
        this.createdDate = createdDate;
        this.resolvedDate = resolvedDate;
    }

    public Integer getReturnRequestId() {
        return returnRequestId;
    }

    public void setReturnRequestId(Integer returnRequestId) {
        this.returnRequestId = returnRequestId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

}
