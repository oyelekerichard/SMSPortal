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
public class LongResponse {

    private List<SMSResponse> messageParts = new ArrayList<>();

    public List<SMSResponse> getMessageParts() {
        return messageParts;
    }

    public void setMessageParts(List<SMSResponse> messageParts) {
        this.messageParts = messageParts;
    }

}
