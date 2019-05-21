/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

/**
 *
 * @author Oluremi Adekanmbi <oluremi.adekanmbi@etranzact.com>
 */
public class Notification {

   private String provider;
   private String dateOfTransaction;
   private String fullName;
   private String providerRef;
   private String transactionRef;
   private String amount;
   private String beneficiary;
   private String userEmail;
   private String password;
   private String loginDateTime;
   private String resellerCode;
   private String link;

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getResellerCode() {
    return resellerCode;
  }

  public void setResellerCode(String resellerCode) {
    this.resellerCode = resellerCode;
  }

   public String getLoginDateTime() {
      return loginDateTime;
   }

   public void setLoginDateTime(String loginDateTime) {
      this.loginDateTime = loginDateTime;
   }

   public String getUserEmail() {
      return userEmail;
   }

   public void setUserEmail(String userEmail) {
      this.userEmail = userEmail;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getProvider() {
      return provider;
   }

   public void setProvider(String provider) {
      this.provider = provider;
   }

   public String getDateOfTransaction() {
      return dateOfTransaction;
   }

   public void setDateOfTransaction(String dateOfTransaction) {
      this.dateOfTransaction = dateOfTransaction;
   }

   public String getFullName() {
      return fullName;
   }

   public void setFullName(String fullName) {
      this.fullName = fullName;
   }

   public String getProviderRef() {
      return providerRef;
   }

   public void setProviderRef(String providerRef) {
      this.providerRef = providerRef;
   }

   public String getTransactionRef() {
      return transactionRef;
   }

   public void setTransactionRef(String transactionRef) {
      this.transactionRef = transactionRef;
   }

   public String getAmount() {
      return amount;
   }

   public void setAmount(String amount) {
      this.amount = amount;
   }

   public String getBeneficiary() {
      return beneficiary;
   }

   public void setBeneficiary(String beneficiary) {
      this.beneficiary = beneficiary;
   }

}
