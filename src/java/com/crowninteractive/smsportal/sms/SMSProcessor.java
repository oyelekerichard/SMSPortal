/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.sms;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Charlee
 */
public class SMSProcessor {

   public SMS getMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();

      int resp = 200;
      try {
         String msisdn = msisdn(request.getParameter("msisdn"), "Invalid input msisdn.");
         String scode = str(request.getParameter("scode"), "Invalid message short code.");
         String text = str(request.getParameter("text"), "Invalid text message.");
         //Could be USSD, SMS or IVR.
         String adapter = str(request.getParameter("adapter"), "Invalid message adapter.");
         return new SMS(msisdn, scode, text, adapter, "");
      } catch (IOException ex) {
         resp = 500;
         return null;
      } finally {
         response.setStatus(resp);
         out.println();
         out.close();
      }
   }

   private String str(String str, String msg) throws IOException {
      if (str == null) {
         throw new IOException(msg);
      }
      return str.trim();
   }

   private String msisdn(String msisdn, String msg) throws IOException {
      // Multi country support not included
      if (msisdn == null) {
         throw new IOException(msg);
      }
      if (msisdn.startsWith("0")) {
         return msisdn.substring(1);
      }
      if (msisdn.startsWith("234")) {
         return msisdn.substring(3);
      }
      return msisdn;
   }

   public void sendMessage(String msisdn, String scode, String text)
           throws IllegalArgumentException, Exception {
      System.out.println("MSISDN : " + msisdn);
      System.out.println("SCODE : " + scode);
      System.out.println("TEXT : " + text);
      SmsSender.send(msisdn, scode, text);
      System.out.println("Finished sending SMS >>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<");
   }
}
