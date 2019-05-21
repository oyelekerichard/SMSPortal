/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.model;

import com.crowninteractive.smsportal.enums.MessageReturnType;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Oluremi Adekanmbi <oluremi.adekanmbi@etranzact.com>
 */
@Entity
@Table(name = "ussd_menuitem")
@NamedQueries({
    @NamedQuery(name = "MenuItem.findMenus", query = "SELECT m FROM MenuItem m where m.numberOfMenus <> NULL")})
public class MenuItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String shortCode;
    private String menuMessage;
    private boolean makeServiceCallBefore;
    private String serviceName;
    private String serviceSection;
    private boolean finalMenu;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date modified;
    private String merchantIdentifier;
    private int numberOfMenus;
    @Enumerated(EnumType.STRING)
    private MessageReturnType returnType;
    @JoinColumn(name = "template_id", referencedColumnName = "ID")
    @OneToOne
    private USSDTemplate templateId;

    @OneToOne
    private MenuItem menuItem;

    public MenuItem() {
    }

    public MenuItem(Long id) {
        this.id = id;
    }

    public USSDTemplate getTemplateId() {
        return templateId;
    }

    public void setTemplateId(USSDTemplate templateId) {
        this.templateId = templateId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getMerchantIdentifier() {
        return merchantIdentifier;
    }

    public void setMerchantIdentifier(String merchantIdentifier) {
        this.merchantIdentifier = merchantIdentifier;
    }

    public int getNumberOfMenus() {
        return numberOfMenus;
    }

    public void setNumberOfMenus(int numberOfMenus) {
        this.numberOfMenus = numberOfMenus;
    }

    public MessageReturnType getReturnType() {
        return returnType;
    }

    public void setReturnType(MessageReturnType returnType) {
        this.returnType = returnType;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public String getServiceSection() {
        return serviceSection;
    }

    public void setServiceSection(String serviceSection) {
        this.serviceSection = serviceSection;
    }

    public boolean isFinalMenu() {
        return finalMenu;
    }

    public void setFinalMenu(boolean finalMenu) {
        this.finalMenu = finalMenu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isMakeServiceCallBefore() {
        return makeServiceCallBefore;
    }

    public void setMakeServiceCallBefore(boolean makeServiceCallBefore) {
        this.makeServiceCallBefore = makeServiceCallBefore;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMenuMessage() {
        return menuMessage;
    }

    public void setMenuMessage(String menuMessage) {
        this.menuMessage = menuMessage;
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
        if (!(object instanceof MenuItem)) {
            return false;
        }
        MenuItem other = (MenuItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MenuItem{" + "id=" + id + ", shortCode=" + shortCode + ", menuMessage=" + menuMessage + ", makeServiceCallBefore=" + makeServiceCallBefore + ", serviceName=" + serviceName + ", serviceSection=" + serviceSection + ", finalMenu=" + finalMenu + ", created=" + created + ", modified=" + modified + ", merchantIdentifier=" + merchantIdentifier + ", numberOfMenus=" + numberOfMenus + ", returnType=" + returnType + ", menuItem=" + menuItem + '}';
    }

}
