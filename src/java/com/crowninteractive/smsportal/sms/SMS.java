/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.sms;

/**
 *
 * @author Charlee
 */
public class SMS {

   private final String msisdn;
   private final String scode;
   private final String text;
   private final String adapter;
   private final String uniqueId;

   public SMS(String msisdn, String scode, String text, String adapter, String uniqueId) {
      this.msisdn = msisdn;
      this.scode = scode;
      this.text = text;
      this.adapter = adapter;
      this.uniqueId = uniqueId;
   }

   public String getMsisdn() {
      return msisdn;
   }

   public String getText() {
      return text;
   }

   public String getScode() {
      return scode;
   }

   public String getAdapter() {
      return adapter;
   }

}
