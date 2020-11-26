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
public class LongResponseHolder {

    private List<LongResponse> responses = new ArrayList<>();

    public List<LongResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<LongResponse> responses) {
        this.responses = responses;
    }

}
