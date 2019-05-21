/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

/**
 *
 * @author adekanmbi
 */
public class UCGResponse {

    private Status status;
    private EntityCollection entity;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public EntityCollection getEntity() {
        return entity;
    }

    public void setEntity(EntityCollection entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "UCG{" + "status=" + status + ", entity=" + entity + '}';
    }

}
