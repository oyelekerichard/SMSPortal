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
public class PowerScheduleResponse {

    private int resp;
    private String desc;
    private PowerScheduleSimple object;

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

    public PowerScheduleSimple getObject() {
        return object;
    }

    public void setObject(PowerScheduleSimple object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "PowerScheduleResponse{" + "resp=" + resp + ", desc=" + desc + ", object=" + object + '}';
    }

}
