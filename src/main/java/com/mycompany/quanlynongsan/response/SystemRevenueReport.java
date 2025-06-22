/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.response;

import java.math.BigDecimal;

/**
 *
 * @author nghiem
 */
public class SystemRevenueReport {
    private String timeGroup;
    private BigDecimal farmerRevenue;
    private BigDecimal distributorImportCost;
    private BigDecimal distributorRevenue;
    // getters/setters...

    public SystemRevenueReport() {
    }

    public SystemRevenueReport(String timeGroup, BigDecimal farmerRevenue, BigDecimal distributorImportCost,
            BigDecimal distributorRevenue) {
        this.timeGroup = timeGroup;
        this.farmerRevenue = farmerRevenue;
        this.distributorImportCost = distributorImportCost;
        this.distributorRevenue = distributorRevenue;
    }

    public String getTimeGroup() {
        return timeGroup;
    }

    public void setTimeGroup(String timeGroup) {
        this.timeGroup = timeGroup;
    }

    public BigDecimal getFarmerRevenue() {
        return farmerRevenue;
    }

    public void setFarmerRevenue(BigDecimal farmerRevenue) {
        this.farmerRevenue = farmerRevenue;
    }

    public BigDecimal getDistributorImportCost() {
        return distributorImportCost;
    }

    public void setDistributorImportCost(BigDecimal distributorImportCost) {
        this.distributorImportCost = distributorImportCost;
    }

    public BigDecimal getDistributorRevenue() {
        return distributorRevenue;
    }

    public void setDistributorRevenue(BigDecimal distributorRevenue) {
        this.distributorRevenue = distributorRevenue;
    }

}
