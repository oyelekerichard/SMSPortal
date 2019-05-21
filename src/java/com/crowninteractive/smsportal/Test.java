/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal;

import com.crowninteractive.smsportal.dto.PowerScheduleResponse;
import com.crowninteractive.smsportal.util.HttpUtil;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adekanmbi
 */
public class Test {

    public static void main(String[] args) {
        String retVal2;
        try {
            retVal2 = HttpUtil.sendGet("http://81.26.66.87:8080/integration/findCurrentSchedule/kUfpGw2o9ylbTAEu6seI9BjvN8GsXA");
            PowerScheduleResponse pss = new Gson().fromJson(retVal2, PowerScheduleResponse.class);
            System.out.println(pss.getObject());
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
