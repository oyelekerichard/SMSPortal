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
public class Account {

    private String systemId;
    private String password;

    public Account(String systemId, String password) {
        this.systemId = systemId;
        this.password = password;
    }

    public Account() {
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
