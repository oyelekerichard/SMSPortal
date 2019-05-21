/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Oluremi Adekanmbi
 * <oluremi.adekanmbi@etranzact.com>
 */
public class Data {

    private String provider;
    private String msisdn;
    private String sessionId;
    private String message;

    private String protocol;
    private String subscriber;
    private String _operator;
    private String abonent;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public String getOperator() {
        return _operator;
    }

    public void setOperator(String _operator) {
        this._operator = _operator;
    }

    public String getAbonent() {
        return abonent;
    }

    public void setAbonent(String abonent) {
        this.abonent = abonent;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Data fillDto(Data data, String field, String value) {
        //provider=airtel&msisdn=2348024675639&sessionid=14568284374231393&message=*833*400

        if (field.equalsIgnoreCase("provider") || field.equalsIgnoreCase("_operator")) {
            data.setProvider(value);
            data.setOperator(value);
        } else if (field.equalsIgnoreCase("msisdn") || field.equalsIgnoreCase("subscriber")) {
            data.setMsisdn(value);
            data.setSubscriber(value);
        } else if (field.equalsIgnoreCase("sessionid")) {
            data.setSessionId(value);
        } else if (field.equalsIgnoreCase("message")) {
            data.setMessage(value);
        } else if (field.equalsIgnoreCase("protocol")) {
            data.setProtocol(value);
        } else if (field.equalsIgnoreCase("abonent")) {
            data.setAbonent(value);
        }
        return data;
    }

    @Override
    public String toString() {
        return "Data{" + "provider=" + provider + ", msisdn=" + msisdn + ", sessionId=" + sessionId + ", message=" + message + ", protocol=" + protocol + ", subscriber=" + subscriber + ", _operator=" + _operator + ", abonent=" + abonent + ", request=" + request + ", response=" + response + '}';
    }

}
