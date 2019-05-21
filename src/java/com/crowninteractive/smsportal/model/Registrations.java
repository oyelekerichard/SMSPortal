/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.model;

import com.crowninteractive.smsportal.enums.MeterType;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

/**
 *
 * @author Adekanmbi Oluremi adekanmbi.oluremi@gmail.com +2348098753155
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Registrations.findAll", query = "SELECT s FROM Registrations s WHERE s.phone =:phone AND s.meterType =:meterType")})
public class Registrations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    private String accountORMeterNumber;
    @Enumerated(EnumType.STRING)
    private MeterType meterType;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;

    public Registrations() {
    }

    public Registrations(String phone, String accountORMeterNumber, MeterType meterType) {
        this.phone = phone;
        this.accountORMeterNumber = accountORMeterNumber;
        this.meterType = meterType;
        this.createdDate = DateTimeUtil.getCurrentDate();
    }

    public MeterType getMeterType() {
        return meterType;
    }

    public void setMeterType(MeterType meterType) {
        this.meterType = meterType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccountORMeterNumber() {
        return accountORMeterNumber;
    }

    public void setAccountORMeterNumber(String accountORMeterNumber) {
        this.accountORMeterNumber = accountORMeterNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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
        if (!(object instanceof Registrations)) {
            return false;
        }
        Registrations other = (Registrations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Registrations{" + "id=" + id + ", phone=" + phone + ", accountORMeterNumber=" + accountORMeterNumber + ", meterType=" + meterType + ", createdDate=" + createdDate + '}';
    }
}
