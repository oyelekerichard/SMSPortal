/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.util;

import com.crowninteractive.smsportal.dto.Response;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

/**
 *
 * @author Adekanmbi Oluremi adekanmbi.oluremi@gmail.com +2348098753155
 */
public class Validate {

    private static final Logger LOG = Logger.getLogger(Validate.class);

    public static String validateCustomer(String accountNosOrMeterNumber, boolean isPrepaid) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://172.29.14.3:8080/UCG/API/rpc/Endpoint/validateCustomer?accountOrMeterNumber=");
        //sb.append("http://172.29.14.110:28080/UCG/API/rpc/Endpoint/validateCustomer?accountOrMeterNumber=");
        sb.append(accountNosOrMeterNumber);
        sb.append("&isPrepaid=").append(isPrepaid);
        String retVal;
        try {
            return HttpUtil.sendPost(sb.toString(), "");
        } catch (Exception ex) {
            return "";
        }
    }

    public static String validateCustomerStatus(String accountNosOrMeterNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://172.29.14.3:8080/UCG/API/rpc/Endpoint/customerValidationStatus?accountOrMeterNumber=");
        //sb.append("http://172.29.14.110:28080/UCG/API/rpc/Endpoint/customerValidationStatus?accountOrMeterNumber=");
        sb.append(accountNosOrMeterNumber);
        try {
            String sendGet = HttpUtil.sendGet(sb.toString());
            System.out.println(sendGet);
            return sendGet;
        } catch (Exception ex) {
            return "";
        }
    }

    public static String validateTestCustomer(String accountNosOrMeterNumber, boolean isPrepaid) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://172.29.14.110:8080/UCG/API/rpc/Endpoint/validateCustomer?accountOrMeterNumber=");
        sb.append(accountNosOrMeterNumber);
        sb.append("&isPrepaid=").append(isPrepaid);
        String retVal;
        try {
            return HttpUtil.sendPost(sb.toString(), "");
        } catch (Exception ex) {
            return "";
        }
    }

    public static String validateTestCustomerStatus(String accountNosOrMeterNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://172.29.14.110:8080/UCG/API/rpc/Endpoint/customerValidationStatus?accountOrMeterNumber=");
        sb.append(accountNosOrMeterNumber);
        try {
            String sendGet = HttpUtil.sendGet(sb.toString());
            System.out.println(sendGet);
            return sendGet;
        } catch (Exception ex) {
            return "";
        }
    }

    public static Response process(String accountNosOrMeterNumber, boolean isPrepaid) {

        try {
            validateCustomer(accountNosOrMeterNumber, isPrepaid);
            System.out.println("Thread is sleeping");
            Thread.sleep(60000);
            System.out.println("Thread has woken up!!!");
        } catch (InterruptedException | NullPointerException ex) {
        }
        String validateCustomerStatus = validateCustomerStatus(accountNosOrMeterNumber);
        int counter = 1;
        while (counter <= 10 && validateCustomerStatus.isEmpty()) {
            System.out.println("Counter : " + counter);
            validateCustomerStatus = validateCustomerStatus(accountNosOrMeterNumber);
            counter++;
        }
        System.out.println("String returned is " + validateCustomerStatus);
        return new Gson().fromJson(validateCustomerStatus, Response.class);
    }

    public static Response processTest(String accountNosOrMeterNumber, boolean isPrepaid) {

        try {
            validateTestCustomer(accountNosOrMeterNumber, isPrepaid);
            System.out.println("Thread is sleeping");
            Thread.sleep(60000);
            System.out.println("Thread has woken up!!!");
        } catch (InterruptedException | NullPointerException ex) {
        }
        String validateCustomerStatus = validateTestCustomerStatus(accountNosOrMeterNumber);
        int counter = 1;
        while (counter <= 10 && validateCustomerStatus.isEmpty()) {
            System.out.println("Counter : " + counter);
            validateCustomerStatus = validateTestCustomerStatus(accountNosOrMeterNumber);
            counter++;
        }
        System.out.println("String returned is " + validateCustomerStatus);
        return new Gson().fromJson(validateCustomerStatus, Response.class);
    }

    public static void main(String[] args) {
        Response process = process("0101150450525", true);
        System.out.println(process.getStatus().getCode());
        System.out.println(process.getStatus().getDescription());
    }
}
