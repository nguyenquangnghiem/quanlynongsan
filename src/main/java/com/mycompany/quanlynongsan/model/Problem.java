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
public class Problem {
    Integer problemId;
    String name;
    String reason;
    Boolean isResolved;
    Integer orderId;
    Date createdDate;

    public Problem() {
    }

    public Problem(Integer problemId, String name, String reason, Boolean isResolved, Integer orderId, Date createdDate) {
        this.problemId = problemId;
        this.name = name;
        this.reason = reason;
        this.isResolved = isResolved;
        this.orderId = orderId;
        this.createdDate = createdDate;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    
}
