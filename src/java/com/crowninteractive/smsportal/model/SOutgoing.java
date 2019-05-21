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
@Table(name = "SOutgoing", catalog = "smsdb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SOutgoing.findAll", query = "SELECT o FROM SOutgoing o")
    , @NamedQuery(name = "SOutgoing.findById", query = "SELECT o FROM SOutgoing o WHERE o.id = :id")
    , @NamedQuery(name = "SOutgoing.findByAdapter", query = "SELECT o FROM SOutgoing o WHERE o.adapter = :adapter")
    , @NamedQuery(name = "SOutgoing.findByMsisdn", query = "SELECT o FROM SOutgoing o WHERE o.msisdn = :msisdn")
    , @NamedQuery(name = "SOutgoing.findByScode", query = "SELECT o FROM SOutgoing o WHERE o.scode = :scode")
    , @NamedQuery(name = "SOutgoing.findBySent", query = "SELECT o FROM SOutgoing o WHERE o.sent = :sent")
    , @NamedQuery(name = "SOutgoing.findByText", query = "SELECT o FROM SOutgoing o WHERE o.text = :text")
    , @NamedQuery(name = "SOutgoing.findByUniqueId", query = "SELECT o FROM SOutgoing o WHERE o.uniqueId = :uniqueId")})
public class SOutgoing implements Serializable {

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
    @Size(max = 255)
    @Column(name = "scode", length = 255)
    private String scode;
    @Column(name = "sent")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sent;
    @Size(max = 255)
    @Column(name = "text", length = 255)
    private String text;
    @Size(max = 255)
    @Column(name = "uniqueId", length = 255)
    private String uniqueId;

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

    public SOutgoing() {
    }

    public SOutgoing(Long id) {
        this.id = id;
    }

    public SOutgoing(SMS sms, String uniqueTransId, String returnedMessage) {
        this.msisdn = sms.getMsisdn();
        this.scode = sms.getScode();
        this.text = returnedMessage;
        this.adapter = sms.getAdapter();
        this.uniqueId = uniqueTransId;
        this.sent = DateTimeUtil.getCurrentDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
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
        if (!(object instanceof SOutgoing)) {
            return false;
        }
        SOutgoing other = (SOutgoing) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.crowninteractive.smsportal.model.Outgoing[ id=" + id + " ]";
    }

}
