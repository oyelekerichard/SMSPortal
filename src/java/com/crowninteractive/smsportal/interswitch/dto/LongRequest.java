/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.interswitch.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adekanmbi
 */
public class LongRequest {

    private Account account;
    private List<SMS> requests = new ArrayList<>();
    private String src;
    private String text;

//{
//  "account": {
//    "password": "string",
//    "systemId": "string"
//  },
//  "destinations": [
//    "string"
//  ],
//  "src": "string",
//  "text": "string"
//}
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<SMS> getRequests() {
        return requests;
    }

    public void setRequests(List<SMS> requests) {
        this.requests = requests;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
