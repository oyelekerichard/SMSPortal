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
public class FeederStatusResponse {

    private int resp;
    private String desc;
    private FeederStatus object;

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

    public FeederStatus getObject() {
        return object;
    }

    public void setObject(FeederStatus object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "FeederStatusResponse{" + "resp=" + resp + ", desc=" + desc + ", object=" + object + '}';
    }

}
