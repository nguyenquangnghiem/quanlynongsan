/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.response;

import java.time.LocalDateTime;

public class BehaviorLog {
    private Integer userId;
    private String fullName;
    private String email;
    private Integer behaviorId;
    private String code;
    private String description;
    private LocalDateTime time;

    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getBehaviorId() { return behaviorId; }
    public void setBehaviorId(Integer behaviorId) { this.behaviorId = behaviorId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
}
