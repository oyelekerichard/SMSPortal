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
public class Response {

   private Status status;
   private boolean entity;

   public Status getStatus() {
      return status;
   }

   public void setStatus(Status status) {
      this.status = status;
   }

   public boolean isEntity() {
      return entity;
   }

   public void setEntity(boolean entity) {
      this.entity = entity;
   }

   @Override
   public String toString() {
      return "Response{" + "status=" + status + ", entity=" + entity + '}';
   }

}
