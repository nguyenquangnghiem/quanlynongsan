/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.model;

/**
 *
 * @author nghiem
 */
public class HasBehavior {
    Integer userId;
    Integer behaviorId;

    public HasBehavior() {
    }

    public HasBehavior(Integer userId, Integer behaviorId) {
        this.userId = userId;
        this.behaviorId = behaviorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(Integer behaviorId) {
        this.behaviorId = behaviorId;
    }

}
