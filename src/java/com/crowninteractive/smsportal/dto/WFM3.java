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
public class WFM3 {
//[{"orderId":"20229588","ChargeName":"RECONNECTION FEE","address":"BLK 103 LEKKI PENNISULA, PL7, LEKKI","billingAccount":"1011901920-01","debtOnAccount":"5000.0"}]}

    private String orderId;
    private String ChargeName;
    private String address;
    private String billingAccount;
    private String debtOnAccount;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getChargeName() {
        return ChargeName;
    }

    public void setChargeName(String ChargeName) {
        this.ChargeName = ChargeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBillingAccount() {
        return billingAccount;
    }

    public void setBillingAccount(String billingAccount) {
        this.billingAccount = billingAccount;
    }

    public String getDebtOnAccount() {
        return debtOnAccount;
    }

    public void setDebtOnAccount(String debtOnAccount) {
        this.debtOnAccount = debtOnAccount;
    }

}
