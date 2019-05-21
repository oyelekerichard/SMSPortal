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
public class MtechRequest {

    private String Type;
    private String SessionId;
    private String Mobile;
    private String ServiceCode;
    private String Operator;
    private int Sequence;
    private boolean DirectSubmenu;
    private String Message;

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String SessionId) {
        this.SessionId = SessionId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getServiceCode() {
        return ServiceCode;
    }

    public void setServiceCode(String ServiceCode) {
        this.ServiceCode = ServiceCode;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String Operator) {
        this.Operator = Operator;
    }

    public int getSequence() {
        return Sequence;
    }

    public void setSequence(int Sequence) {
        this.Sequence = Sequence;
    }

    public boolean isDirectSubmenu() {
        return DirectSubmenu;
    }

    public void setDirectSubmenu(boolean DirectSubmenu) {
        this.DirectSubmenu = DirectSubmenu;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    @Override
    public String toString() {
        return "MtechRequest{" + "Type=" + Type + ", SessionId=" + SessionId + ", Mobile=" + Mobile + ", ServiceCode=" + ServiceCode + ", Operator=" + Operator + ", Sequence=" + Sequence + ", DirectSubmenu=" + DirectSubmenu + '}';
    }

}
