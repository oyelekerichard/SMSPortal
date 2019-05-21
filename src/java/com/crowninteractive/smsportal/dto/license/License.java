/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto.license;

/**
 *
 * @author adekanmbi
 */
public class License {

    private String appId;
    private String encrytedData;
    private String passphrase;

    public License() {
    }

    public License(String appId, String encrytedData, String passphrase) {
        this.appId = appId;
        this.encrytedData = encrytedData;
        this.passphrase = passphrase;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEncrytedData() {
        return encrytedData;
    }

    public void setEncrytedData(String encrytedData) {
        this.encrytedData = encrytedData;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    @Override
    public String toString() {
        return "License{" + "appId=" + appId + ", encrytedData=" + encrytedData + ", passphrase=" + passphrase + '}';
    }

}
