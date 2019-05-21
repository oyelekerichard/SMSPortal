/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * NavigationItem manages what is shown as an item to each User.
 *
 * @author dm4
 */
@Entity
@Table(name = "navigation_items")
public class NavigationItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "description", length = 50)
    private String description;
    @Column(name = "psection", length = 50)
    private String pSection;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getpSection() {
        return pSection;
    }

    public void setpSection(String pSection) {
        this.pSection = pSection;
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
        if (!(object instanceof NavigationItem)) {
            return false;
        }
        NavigationItem other = (NavigationItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NavigationItem[ id=" + id + " ]";
    }

}
