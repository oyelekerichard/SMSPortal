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
public class InjectionSubstation implements Serializable {

    private Integer id;
    private String networkAssetNumber;
    private String name;
    private String address;
    private Boolean isActive;
    private String token;
    private String coordinates;
    private Date createdTime;
    private Date updatedTime;
    private String status;
    private String cugLine;

    public InjectionSubstation() {
    }

    public InjectionSubstation(Integer id) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCugLine() {
        return cugLine;
    }

    public void setCugLine(String cugLine) {
        this.cugLine = cugLine;
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
        if (!(object instanceof InjectionSubstation)) {
            return false;
        }
        InjectionSubstation other = (InjectionSubstation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.crowninteractive.emcc.models.InjectionSubstation[ id=" + id + " ]";
    }

}
