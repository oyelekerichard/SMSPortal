/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oluremi Adekanmbi <oluremi.adekanmbi@etranzact.com>
 */
@Entity
@Table(name = "ussd_subscriber", catalog = "smsdb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UssdSubscriber.findAll", query = "SELECT u FROM UssdSubscriber u")
    ,
   @NamedQuery(name = "UssdSubscriber.findById", query = "SELECT u FROM UssdSubscriber u WHERE u.id = :id")
    ,
   @NamedQuery(name = "UssdSubscriber.findByAccountNo", query = "SELECT u FROM UssdSubscriber u WHERE u.accountNo = :accountNo")
    ,
   @NamedQuery(name = "UssdSubscriber.findByActive", query = "SELECT u FROM UssdSubscriber u WHERE u.active = :active")
    ,
   @NamedQuery(name = "UssdSubscriber.findByBankCode", query = "SELECT u FROM UssdSubscriber u WHERE u.bankCode = :bankCode")
    ,
   @NamedQuery(name = "UssdSubscriber.findByCreated", query = "SELECT u FROM UssdSubscriber u WHERE u.created = :created")
    ,
   @NamedQuery(name = "UssdSubscriber.findByLastTranTime", query = "SELECT u FROM UssdSubscriber u WHERE u.lastTranTime = :lastTranTime")
    ,
   @NamedQuery(name = "UssdSubscriber.findByMobileNo", query = "SELECT u FROM UssdSubscriber u WHERE u.mobileNo = :mobileNo")
    ,
   @NamedQuery(name = "UssdSubscriber.findByMobileNoAndAppCode", query = "SELECT u FROM UssdSubscriber u WHERE u.mobileNo = :mobileNo AND u.appcode = :appcode AND u.accountNo =:accountNo")
    ,
   @NamedQuery(name = "UssdSubscriber.findByModified", query = "SELECT u FROM UssdSubscriber u WHERE u.modified = :modified")
    ,
   @NamedQuery(name = "UssdSubscriber.findByAppcode", query = "SELECT u FROM UssdSubscriber u WHERE u.appcode = :appcode")})
public class UssdSubscriber implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "account_no", length = 255)
    private String accountNo;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "bank_code", length = 255)
    private String bankCode;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "lastTranTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTranTime;
    @Column(name = "mobile_no", length = 255)
    private String mobileNo;
    @Column(name = "modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;
    @Column(name = "appcode", length = 25)
    private String appcode;

    public UssdSubscriber() {
    }

    public UssdSubscriber(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastTranTime() {
        return lastTranTime;
    }

    public void setLastTranTime(Date lastTranTime) {
        this.lastTranTime = lastTranTime;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
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
        if (!(object instanceof UssdSubscriber)) {
            return false;
        }
        UssdSubscriber other = (UssdSubscriber) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.etz.ussd389.model.UssdSubscriber[ id=" + id + " ]";
    }

}
