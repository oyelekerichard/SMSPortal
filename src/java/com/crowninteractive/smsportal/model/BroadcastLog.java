/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.model;

import com.crowninteractive.smsportal.util.DateTimeUtil;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "broadcast_log2")
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
    @Column(name = "header", length = 200)
    private String header;
    @Column(name = "provider", length = 200)
    private String provider;
    @Column(name = "status", length = 200)
    private String status;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "updated")
    private Date updated;
    @Column(name = "batch_id", length = 30)
    private String batchId;
    @Column(name = "ticket_id", length = 30)
    private String ticketId;
    @Column(name = "ticket_status", length = 100)
    private String ticketStatus;
    @Column(name = "final_status")
    private Boolean finalStatus = false;

    public BroadcastLog() {
    }

    public BroadcastLog(String msisdn, String message, String channel, String status, String batchId) {
        this.msisdn = msisdn;
        this.message = message;
        this.channel = channel;
        this.status = status;
        this.batchId = batchId;
        this.createDate = DateTimeUtil.getCurrentDate();
    }

    public BroadcastLog(String msisdn, String message, String channel, String status, Date createDate, String batchId, String ticketId) {
        this.msisdn = msisdn;
        this.message = message;
        this.channel = channel;
        this.status = status;
        this.createDate = createDate;
        this.batchId = batchId;
        this.ticketId = ticketId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Boolean getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(Boolean finalStatus) {
        this.finalStatus = finalStatus;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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
