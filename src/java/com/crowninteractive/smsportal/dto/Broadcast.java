/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

import java.util.List;

/**
 *
 * @author adekanmbi
 */
public class Broadcast {

    private List<String> msisdns;
    private String message;
    private String channel;

    public List<String> getMsisdns() {
        return msisdns;
    }

    public void setMsisdns(List<String> msisdns) {
        this.msisdns = msisdns;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}
