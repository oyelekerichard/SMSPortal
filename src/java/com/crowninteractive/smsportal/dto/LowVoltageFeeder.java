/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author
 */
public class LowVoltageFeeder implements Serializable {

    private Integer id;
    private String networkAssetNumber;
    private String name;
    private String status;
    private String formation;
    private Integer repute;
    private String streetCoverage;
    private Integer code;
    private Double kilovolts;
    private Boolean isActive;
    private Boolean isOnline;
    private String token;
    private String coordinates;
    private Date createdTime;
    private Date updatedTime;
    private Integer isPriority;
    private Integer priorityStatus;
    private Date priorityCreatedTime;
    private Integer priorityCreatedBy;
    private Date priorityUpdatedTime;
    private Integer priorityUpdatedBy;
    private String ilsCode;
    private String ilsNumber;
    private String meterNumber;
    private Double goodnessScore;
    private Double noOfHours;

    public LowVoltageFeeder() {
    }

    public LowVoltageFeeder(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNetworkAssetNumber() {
        return networkAssetNumber;
    }

    public void setNetworkAssetNumber(String networkAssetNumber) {
        this.networkAssetNumber = networkAssetNumber;
    }

    public String getIlsCode() {
        return ilsCode;
    }

    public void setIlsCode(String ilsCode) {
        this.ilsCode = ilsCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public Integer getRepute() {
        return repute;
    }

    public void setRepute(Integer repute) {
        this.repute = repute;
    }

    public String getStreetCoverage() {
        return streetCoverage;
    }

    public void setStreetCoverage(String streetCoverage) {
        this.streetCoverage = streetCoverage;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Double getKilovolts() {
        return kilovolts;
    }

    public void setKilovolts(Double kilovolts) {
        this.kilovolts = kilovolts;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
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
        if (!(object instanceof LowVoltageFeeder)) {
            return false;
        }
        LowVoltageFeeder other = (LowVoltageFeeder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.crowninteractive.emcc.models.LowVoltageFeeder[ id=" + id + " ]";
    }

    public Integer getIsPriority() {
        return isPriority;
    }

    public void setIsPriority(Integer isPriority) {
        this.isPriority = isPriority;
    }

    public Integer getPriorityStatus() {
        return priorityStatus;
    }

    public void setPriorityStatus(Integer priorityStatus) {
        this.priorityStatus = priorityStatus;
    }

    public Date getPriorityCreatedTime() {
        return priorityCreatedTime;
    }

    public void setPriorityCreatedTime(Date priorityCreatedTime) {
        this.priorityCreatedTime = priorityCreatedTime;
    }

    public Integer getPriorityCreatedBy() {
        return priorityCreatedBy;
    }

    public void setPriorityCreatedBy(Integer priorityCreatedBy) {
        this.priorityCreatedBy = priorityCreatedBy;
    }

    public Date getPriorityUpdatedTime() {
        return priorityUpdatedTime;
    }

    public void setPriorityUpdatedTime(Date priorityUpdatedTime) {
        this.priorityUpdatedTime = priorityUpdatedTime;
    }

    public Integer getPriorityUpdatedBy() {
        return priorityUpdatedBy;
    }

    public void setPriorityUpdatedBy(Integer priorityUpdatedBy) {
        this.priorityUpdatedBy = priorityUpdatedBy;
    }

    public String getIlsNumber() {
        return ilsNumber;
    }

    public void setIlsNumber(String ilsNumber) {
        this.ilsNumber = ilsNumber;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public Double getGoodnessScore() {
        return goodnessScore;
    }

    public void setGoodnessScore(Double goodnessScore) {
        this.goodnessScore = goodnessScore;
    }

    public Double getNoOfHours() {
        return noOfHours;
    }

    public void setNoOfHours(Double noOfHours) {
        this.noOfHours = noOfHours;
    }

}
