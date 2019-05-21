/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adekanmbi
 */
public class WFMResponse {

    private int resp;
    private String desc;
    private List<WFM> object = new ArrayList<>();

    public int getResp() {
        return resp;
    }

    public void setResp(int resp) {
        this.resp = resp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<WFM> getObject() {
        return object;
    }

    public void setObject(List<WFM> object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "WFMResponse{" + "resp=" + resp + ", desc=" + desc + ", object=" + object + '}';
    }

}
