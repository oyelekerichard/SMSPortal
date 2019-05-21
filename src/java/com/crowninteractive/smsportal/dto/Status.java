/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

/**
 *
 * @author Adekanmbi Oluremi adekanmbi.oluremi@gmail.com +2348098753155
 */
public class Status {

   private int code;
   private String description;

   public int getCode() {
      return code;
   }

   public void setCode(int code) {
      this.code = code;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   @Override
   public String toString() {
      return "Status{" + "code=" + code + ", description=" + description + '}';
   }

}
