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
public class SMS {

    private String dest;
    private String src;
    private String text;
    private Boolean unicode = false;

    public SMS() {
    }

    public SMS(String src) {
        this.src = src;
    }

    public SMS(String dest, String src, String text) {
        this.dest = dest;
        this.src = src;
        this.text = text;
    }

    public Boolean getUnicode() {
        return unicode;
    }

    public void setUnicode(Boolean unicode) {
        this.unicode = unicode;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
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
