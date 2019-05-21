/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author adekanmbi
 */
public class FeederStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LowVoltageFeeder lowVoltageFeederId;
    private InjectionSubstation injectionSubstationId;
    private String status;
    private String controllerStatus;
    private Date created;
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LowVoltageFeeder getLowVoltageFeederId() {
        return lowVoltageFeederId;
    }

    public void setLowVoltageFeederId(LowVoltageFeeder lowVoltageFeederId) {
        this.lowVoltageFeederId = lowVoltageFeederId;
    }

    public InjectionSubstation getInjectionSubstationId() {
        return injectionSubstationId;
    }

    public void setInjectionSubstationId(InjectionSubstation injectionSubstationId) {
        this.injectionSubstationId = injectionSubstationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getControllerStatus() {
        return controllerStatus;
    }

    public void setControllerStatus(String controllerStatus) {
        this.controllerStatus = controllerStatus;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
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
        if (!(object instanceof FeederStatus)) {
            return false;
        }
        FeederStatus other = (FeederStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.crowninteractive.emcc.models.FeederStatus[ id=" + id + " ]";
    }

}
