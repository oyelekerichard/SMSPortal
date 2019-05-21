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
public class WFM {

    private String name;
    private String token;
    private String statusToken;
    private String statusName;
    private String workOrderToken;
    private String comment;
    private int ticketId;

    private String orderId;
    private String ChargeName;
    private String address;
    private String billingAccount;
    private String debtOnAccount;

    public WFM() {
    }

    public WFM(String statusToken, String statusName, int ticketId) {
        this.statusToken = statusToken;
        this.statusName = statusName;
        this.ticketId = ticketId;
    }

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

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatusToken() {
        return statusToken;
    }

    public void setStatusToken(String statusToken) {
        this.statusToken = statusToken;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getWorkOrderToken() {
        return workOrderToken;
    }

    public void setWorkOrderToken(String workOrderToken) {
        this.workOrderToken = workOrderToken;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "WFM{" + "statusToken=" + statusToken + ", statusName=" + statusName + ", workOrderToken=" + workOrderToken + ", comment=" + comment + '}';
    }

}
