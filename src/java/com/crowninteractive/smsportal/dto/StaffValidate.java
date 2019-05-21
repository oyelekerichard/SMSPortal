/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

/**
 *
 * @author Adekanmbi Oluremi
 * adekanmbi.oluremi@gmail.com
 * +2348098753155
 */
public class StaffValidate {
  private boolean success;
  private StaffDetail[] data;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public StaffDetail[] getData() {
    return data;
  }

  public void setData(StaffDetail[] data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "StaffValidate{" + "success=" + success + ", staffDetails=" + data + '}';
  }

}
