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
public class Contact {
    Integer contactId;
    String fullName;
    String phoneNumber;
    String description;
    Integer receiverId;
    Date createdDate;

    public Contact() {
    }

    public Contact(Integer contactId, String fullName, String phoneNumber, String description, Integer receiverId, Date createdDate) {
        this.contactId = contactId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.receiverId = receiverId;
        this.createdDate = createdDate;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    
    
}
