/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Oluremi Adekanmbi <oluremi.adekanmbi@etranzact.com>
 */
//@Entity
public class ShortCode implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private String shortCode;
   private String className;
   private boolean hasChildrenMenu;
   private String bankShortName;
   private String bankCode;
   private String appId;
   private String description;
   @OneToMany
   private List<MenuItem> menuItems;

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getBankShortName() {
      return bankShortName;
   }

   public void setBankShortName(String bankShortName) {
      this.bankShortName = bankShortName;
   }

   public String getBankCode() {
      return bankCode;
   }

   public void setBankCode(String bankCode) {
      this.bankCode = bankCode;
   }

   public String getAppId() {
      return appId;
   }

   public void setAppId(String appId) {
      this.appId = appId;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getShortCode() {
      return shortCode;
   }

   public void setShortCode(String shortCode) {
      this.shortCode = shortCode;
   }

   public String getClassName() {
      return className;
   }

   public void setClassName(String className) {
      this.className = className;
   }

   public boolean isHasChildrenMenu() {
      return hasChildrenMenu;
   }

   public void setHasChildrenMenu(boolean hasChildrenMenu) {
      this.hasChildrenMenu = hasChildrenMenu;
   }

   public List<MenuItem> getMenuItems() {
      return menuItems;
   }

   public void setMenuItems(List<MenuItem> menuItems) {
      this.menuItems = menuItems;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (id != null ? id.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof ShortCode)) {
         return false;
      }
      ShortCode other = (ShortCode) object;
      if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "com.etz.ussd.model.ShortCode[ id=" + id + " ]";
   }

}
