/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.interswitch.dto;

/**
 *
 * @author adekanmbi
 */
public class SMSRequest {

    private Account account;
    private SMS sms;

    public SMSRequest() {
    }

    public SMSRequest(Account account, SMS sms) {
        this.account = account;
        this.sms = sms;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public SMS getSms() {
        return sms;
    }

    public void setSms(SMS sms) {
        this.sms = sms;
    }

}
