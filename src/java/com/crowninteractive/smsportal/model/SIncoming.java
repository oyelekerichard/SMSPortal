/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.model;

import com.crowninteractive.smsportal.sms.SMS;
import com.crowninteractive.smsportal.util.DateTimeUtil;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author adekanmbi
 */
@Entity
@Table(name = "SIncoming", catalog = "smsdb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SIncoming.findAll", query = "SELECT i FROM SIncoming i")
    , @NamedQuery(name = "SIncoming.findById", query = "SELECT i FROM SIncoming i WHERE i.id = :id")
    , @NamedQuery(name = "SIncoming.findByAdapter", query = "SELECT i FROM SIncoming i WHERE i.adapter = :adapter")
    , @NamedQuery(name = "SIncoming.findByMsisdn", query = "SELECT i FROM SIncoming i WHERE i.msisdn = :msisdn")
    , @NamedQuery(name = "SIncoming.findByRecieved", query = "SELECT i FROM SIncoming i WHERE i.recieved = :recieved")
    , @NamedQuery(name = "SIncoming.findByScode", query = "SELECT i FROM SIncoming i WHERE i.scode = :scode")
    , @NamedQuery(name = "SIncoming.findByText", query = "SELECT i FROM SIncoming i WHERE i.text = :text")
    , @NamedQuery(name = "SIncoming.findByUniqueId", query = "SELECT i FROM SIncoming i WHERE i.uniqueId = :uniqueId")})
public class SIncoming implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 255)
    @Column(name = "adapter", length = 255)
    private String adapter;
    @Size(max = 255)
    @Column(name = "msisdn", length = 255)
    private String msisdn;
    @Column(name = "recieved")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recieved;
    @Size(max = 255)
    @Column(name = "scode", length = 255)
    private String scode;
    @Size(max = 255)
    @Column(name = "text", length = 255)
    private String text;
    @Size(max = 255)
    @Column(name = "uniqueId", length = 255)
    private String uniqueId;

    public SIncoming() {
    }

    public SIncoming(Long id) {
        this.id = id;
    }

    public SIncoming(SMS sms, String uniqueTransId) {
        this.msisdn = sms.getMsisdn();
        this.scode = sms.getScode();
        this.text = sms.getText();
        this.adapter = sms.getAdapter();
        this.uniqueId = uniqueTransId;
        this.recieved = DateTimeUtil.getCurrentDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdapter() {
        return adapter;
    }

    public void setAdapter(String adapter) {
        this.adapter = adapter;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Date getRecieved() {
        return recieved;
    }

    public void setRecieved(Date recieved) {
        this.recieved = recieved;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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
        if (!(object instanceof SIncoming)) {
            return false;
        }
        SIncoming other = (SIncoming) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.crowninteractive.smsportal.model.Incoming[ id=" + id + " ]";
    }

}
