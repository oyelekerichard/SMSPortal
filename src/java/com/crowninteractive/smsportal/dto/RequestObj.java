/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

/**
 *
 * @author adekanmbi
 */
public class RequestObj {

    private Long itemId;
    private String itemDetails;
    private int stockedValue;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(String itemDetails) {
        this.itemDetails = itemDetails;
    }

    public int getStockedValue() {
        return stockedValue;
    }

    public void setStockedValue(int stockedValue) {
        this.stockedValue = stockedValue;
    }

}
