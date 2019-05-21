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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author adekanmbi
 */
@Entity
@Table(name = "ussd_transaction", catalog = "smsdb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UssdTransaction.findAll", query = "SELECT u FROM UssdTransaction u")
    , @NamedQuery(name = "UssdTransaction.findById", query = "SELECT u FROM UssdTransaction u WHERE u.id = :id")
    , @NamedQuery(name = "UssdTransaction.findByMsisdn", query = "SELECT u FROM UssdTransaction u WHERE u.msisdn = :msisdn")
    , @NamedQuery(name = "UssdTransaction.findByNetwork", query = "SELECT u FROM UssdTransaction u WHERE u.network = :network")
    , @NamedQuery(name = "UssdTransaction.findByIncomingMessage", query = "SELECT u FROM UssdTransaction u WHERE u.incomingMessage = :incomingMessage")
    , @NamedQuery(name = "UssdTransaction.findByOutgoingMessage", query = "SELECT u FROM UssdTransaction u WHERE u.outgoingMessage = :outgoingMessage")
    , @NamedQuery(name = "UssdTransaction.findByIncomingTime", query = "SELECT u FROM UssdTransaction u WHERE u.incomingTime = :incomingTime")
    , @NamedQuery(name = "UssdTransaction.findByOutgoingTime", query = "SELECT u FROM UssdTransaction u WHERE u.outgoingTime = :outgoingTime")
    , @NamedQuery(name = "UssdTransaction.findByReference", query = "SELECT u FROM UssdTransaction u WHERE u.reference = :reference")
    , @NamedQuery(name = "UssdTransaction.findDistinctUsers", query = "SELECT u FROM UssdTransaction u GROUP BY u.msisdn")
    , @NamedQuery(name = "UssdTransaction.findDistinctUsersByDate", query = "SELECT u FROM UssdTransaction u WHERE u.incomingTime BETWEEN :startDate AND :endDate GROUP BY u.msisdn")
    , @NamedQuery(name = "UssdTransaction.findDistinctTransaction", query = "SELECT DISTINCT u FROM UssdTransaction u GROUP BY u.sessionid ORDER BY u.incomingTime DESC")
    , @NamedQuery(name = "UssdTransaction.findDistinctTransactionByDate", query = "SELECT DISTINCT u FROM UssdTransaction u WHERE u.incomingTime BETWEEN :startDate AND :endDate GROUP BY u.sessionid ORDER BY u.incomingTime DESC")
    , @NamedQuery(name = "UssdTransaction.findBySessionid", query = "SELECT u FROM UssdTransaction u WHERE u.sessionid = :sessionid")})
public class UssdTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "msisdn", nullable = false, length = 50)
    private String msisdn;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "network", nullable = false, length = 20)
    private String network;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "incoming_message", nullable = false, length = 50)
    private String incomingMessage;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "reference", nullable = false, length = 50)
    private String reference;
    @Size(min = 1, max = 200)
    @Column(name = "outgoing_message", length = 200)
    private String outgoingMessage;
    @Column(name = "incoming_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date incomingTime;
    @Column(name = "outgoing_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date outgoingTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "sessionid", nullable = false, length = 50)
    private String sessionid;

    @Column(name = "staff_id")
    private String staffId;
    @Column(name = "staff_code")
    private String staffCode;
    @Column(name = "staff_name")
    private String staffName;
    @Column(name = "staff_phone")
    private String staffPhone;
    @Column(name = "department")
    private String department;
    @Column(name = "job_title")
    private String jobTitle;
    @Column(name = "district")
    private String district;
    @Column(name = "staff_status")
    private String status;

    public UssdTransaction() {
    }

    public UssdTransaction(Integer id) {
        this.id = id;
    }

    public UssdTransaction(Integer id, String msisdn, String network, String incomingMessage, String outgoingMessage, String sessionid) {
        this.id = id;
        this.msisdn = msisdn;
        this.network = network;
        this.incomingMessage = incomingMessage;
        this.outgoingMessage = outgoingMessage;
        this.sessionid = sessionid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getIncomingMessage() {
        return incomingMessage;
    }

    public void setIncomingMessage(String incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    public String getOutgoingMessage() {
        return outgoingMessage;
    }

    public void setOutgoingMessage(String outgoingMessage) {
        this.outgoingMessage = outgoingMessage;
    }

    public Date getIncomingTime() {
        return incomingTime;
    }

    public void setIncomingTime(Date incomingTime) {
        this.incomingTime = incomingTime;
    }

    public Date getOutgoingTime() {
        return outgoingTime;
    }

    public void setOutgoingTime(Date outgoingTime) {
        this.outgoingTime = outgoingTime;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
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
        if (!(object instanceof UssdTransaction)) {
            return false;
        }
        UssdTransaction other = (UssdTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UssdTransaction{" + "id=" + id + ", msisdn=" + msisdn + ", network=" + network + ", incomingMessage=" + incomingMessage + ", outgoingMessage=" + outgoingMessage + ", incomingTime=" + incomingTime + ", outgoingTime=" + outgoingTime + ", sessionid=" + sessionid + '}';
    }

}
