/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.response;

import java.util.Date;

/**
 *
 * @author nghiem
 */
public class StockReport {

    private Date date;
    private int importedQuantity;
    private int exportedQuantity;
    private int stockRemaining;

    public StockReport(Date date, int importedQuantity, int exportedQuantity, int stockRemaining) {
        this.date = date;
        this.importedQuantity = importedQuantity;
        this.exportedQuantity = exportedQuantity;
        this.stockRemaining = stockRemaining;
    }

    public StockReport() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getImportedQuantity() {
        return importedQuantity;
    }

    public void setImportedQuantity(int importedQuantity) {
        this.importedQuantity = importedQuantity;
    }

    public int getExportedQuantity() {
        return exportedQuantity;
    }

    public void setExportedQuantity(int exportedQuantity) {
        this.exportedQuantity = exportedQuantity;
    }

    public int getStockRemaining() {
        return stockRemaining;
    }

    public void setStockRemaining(int stockRemaining) {
        this.stockRemaining = stockRemaining;
    }

}
