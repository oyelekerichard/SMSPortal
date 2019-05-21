/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

import java.io.Serializable;

/**
 *
 * @author USER
 */
public class PowerScheduleSimple implements Serializable {

    private Integer id;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String repeatType;
    private String repeatEnd;
    private String approvedTime;
    private String activatedTime;
    private Boolean isActive;
    private String token;
    private String createdTime;
    private String updatedTime;
    private int approvalCode;
    private String approvalStatus; //Possible Values = SMS_SENT, SMS_INVALID_CODE, SMS_ACCEPTED, SMS_INVALID_STAFF_ID, EMCC_USER
    private LowVoltageFeeder lowVoltageFeederId;
    private Integer updatedBy;
    private Integer createdBy;
    private Integer approvedBy;
    private Integer activatedBy;

    public PowerScheduleSimple() {
    }

    public PowerScheduleSimple(Integer id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRepeatEnd() {
        return repeatEnd;
    }

    public void setRepeatEnd(String repeatEnd) {
        this.repeatEnd = repeatEnd;
    }

    public String getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime;
    }

    public String getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(String activatedTime) {
        this.activatedTime = activatedTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(int approvalCode) {
        this.approvalCode = approvalCode;
    }

    public LowVoltageFeeder getLowVoltageFeederId() {
        return lowVoltageFeederId;
    }

    public void setLowVoltageFeederId(LowVoltageFeeder lowVoltageFeederId) {
        this.lowVoltageFeederId = lowVoltageFeederId;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Integer getActivatedBy() {
        return activatedBy;
    }

    public void setActivatedBy(Integer activatedBy) {
        this.activatedBy = activatedBy;
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
        if (!(object instanceof PowerScheduleSimple)) {
            return false;
        }
        PowerScheduleSimple other = (PowerScheduleSimple) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PowerScheduleSimple{" + "id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", startTime=" + startTime + ", endTime=" + endTime + ", repeatType=" + repeatType + ", repeatEnd=" + repeatEnd + ", approvedTime=" + approvedTime + ", activatedTime=" + activatedTime + ", isActive=" + isActive + ", token=" + token + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + ", approvalCode=" + approvalCode + ", approvalStatus=" + approvalStatus + ", lowVoltageFeederId=" + lowVoltageFeederId + ", updatedBy=" + updatedBy + ", createdBy=" + createdBy + ", approvedBy=" + approvedBy + ", activatedBy=" + activatedBy + '}';
    }

}
