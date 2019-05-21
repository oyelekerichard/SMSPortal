/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

import java.util.Objects;

/**
 *
 * @author adekanmbi
 */
public class DeliveryReport {

    private String date;
    private String delivrd;
    private String unknown;
    private String expired;
    private String undeliv;

    public DeliveryReport(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDelivrd() {
        return delivrd;
    }

    public void setDelivrd(String delivrd) {
        this.delivrd = delivrd;
    }

    public String getUnknown() {
        return unknown;
    }

    public void setUnknown(String unknown) {
        this.unknown = unknown;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getUndeliv() {
        return undeliv;
    }

    public void setUndeliv(String undeliv) {
        this.undeliv = undeliv;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DeliveryReport other = (DeliveryReport) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DeliveryReport{" + "date=" + date + ", delivrd=" + delivrd + ", unknown=" + unknown + ", expired=" + expired + ", undeliv=" + undeliv + '}';
    }

}
