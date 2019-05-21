/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.model;

import com.crowninteractive.smsportal.enums.Status;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author adekanmbi
 */
@Entity
@Table(name = "sms_broadcast_log")
public class BroadcastLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "msisdn", length = 15)
    private String msisdn;
    @Column(name = "message", length = 200)
    private String message;
    @Column(name = "channel", length = 200)
    private String channel;
    @Column(name = "status", length = 200)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "batch_id", length = 30)
    private String batchId;

    public BroadcastLog() {
    }

    public BroadcastLog(String msisdn, String message, String channel, Status status, String batchId) {
        this.msisdn = msisdn;
        this.message = message;
        this.channel = channel;
        this.status = status;
        this.batchId = batchId;
        this.createDate = DateTimeUtil.getCurrentDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
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
        if (!(object instanceof BroadcastLog)) {
            return false;
        }
        BroadcastLog other = (BroadcastLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.crowninteractive.smsportal.model.BroadcastLog[ id=" + id + " ]";
    }

}
