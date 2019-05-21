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
public class Entity {

    private int id;
    private int amount;
    private long confirmationTime;
    private long initiationTime;
    private String paymentPartner;
    private String providerRef;
    private String purpose;
    private int status;
    private String userId;
    private String extraData;
    private String providerMessage;
    private String agentId;
    private String agentName;
    private int convenienceCharge;
    private String merchantId;
    private String secondaryUserId; //accountNumber
    private String tamAgentId;
    private String terminalId;
    private String ucgChannel;
    private String tenantId;
    private String cashOffice;
    private String businessUnit;
    private String customerBusinessUnit; //BusinessUnit
    private String customerAccountType;
    private String tamAgentName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getConfirmationTime() {
        return confirmationTime;
    }

    public void setConfirmationTime(long confirmationTime) {
        this.confirmationTime = confirmationTime;
    }

    public long getInitiationTime() {
        return initiationTime;
    }

    public void setInitiationTime(long initiationTime) {
        this.initiationTime = initiationTime;
    }

    public String getPaymentPartner() {
        return paymentPartner;
    }

    public void setPaymentPartner(String paymentPartner) {
        this.paymentPartner = paymentPartner;
    }

    public String getProviderRef() {
        return providerRef;
    }

    public void setProviderRef(String providerRef) {
        this.providerRef = providerRef;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getProviderMessage() {
        return providerMessage;
    }

    public void setProviderMessage(String providerMessage) {
        this.providerMessage = providerMessage;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getConvenienceCharge() {
        return convenienceCharge;
    }

    public void setConvenienceCharge(int convenienceCharge) {
        this.convenienceCharge = convenienceCharge;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSecondaryUserId() {
        return secondaryUserId;
    }

    public void setSecondaryUserId(String secondaryUserId) {
        this.secondaryUserId = secondaryUserId;
    }

    public String getTamAgentId() {
        return tamAgentId;
    }

    public void setTamAgentId(String tamAgentId) {
        this.tamAgentId = tamAgentId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getUcgChannel() {
        return ucgChannel;
    }

    public void setUcgChannel(String ucgChannel) {
        this.ucgChannel = ucgChannel;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCashOffice() {
        return cashOffice;
    }

    public void setCashOffice(String cashOffice) {
        this.cashOffice = cashOffice;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getCustomerBusinessUnit() {
        return customerBusinessUnit;
    }

    public void setCustomerBusinessUnit(String customerBusinessUnit) {
        this.customerBusinessUnit = customerBusinessUnit;
    }

    public String getCustomerAccountType() {
        return customerAccountType;
    }

    public void setCustomerAccountType(String customerAccountType) {
        this.customerAccountType = customerAccountType;
    }

    public String getTamAgentName() {
        return tamAgentName;
    }

    public void setTamAgentName(String tamAgentName) {
        this.tamAgentName = tamAgentName;
    }

    @Override
    public String toString() {
        return "Entity{" + "id=" + id + ", amount=" + amount + ", confirmationTime=" + confirmationTime + ", initiationTime=" + initiationTime + ", paymentPartner=" + paymentPartner + ", providerRef=" + providerRef + ", purpose=" + purpose + ", status=" + status + ", userId=" + userId + ", extraData=" + extraData + ", providerMessage=" + providerMessage + ", agentId=" + agentId + ", agentName=" + agentName + ", convenienceCharge=" + convenienceCharge + ", merchantId=" + merchantId + ", secondaryUserId=" + secondaryUserId + ", tamAgentId=" + tamAgentId + ", terminalId=" + terminalId + ", ucgChannel=" + ucgChannel + ", tenantId=" + tenantId + ", cashOffice=" + cashOffice + ", businessUnit=" + businessUnit + ", customerBusinessUnit=" + customerBusinessUnit + ", customerAccountType=" + customerAccountType + ", tamAgentName=" + tamAgentName + '}';
    }

}
