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
public class BulkResponse {

    private List<SMSResponse> responses = new ArrayList<>();

    public List<SMSResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<SMSResponse> responses) {
        this.responses = responses;
    }

}
