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
public class MtechResponse {

    private String Message;
    private String Type;
    private String ClientState;

    public MtechResponse() {
    }

    public MtechResponse(String Message, String Type, String ClientState) {
        this.Message = Message;
        this.Type = Type;
        this.ClientState = ClientState;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getClientState() {
        return ClientState;
    }

    public void setClientState(String ClientState) {
        this.ClientState = ClientState;
    }

    @Override
    public String toString() {
        return "MtechResponse{" + "Message=" + Message + ", Type=" + Type + ", ClientState=" + ClientState + '}';
    }

}
