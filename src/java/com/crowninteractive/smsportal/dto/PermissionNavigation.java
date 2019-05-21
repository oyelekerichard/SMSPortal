/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

import com.crowninteractive.smsportal.model.NavigationItem;
import com.crowninteractive.smsportal.model.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adekanmbi
 */
public class PermissionNavigation {

    private List<Permission> permissions = new ArrayList<>();
    private List<NavigationItem> navigations = new ArrayList<>();

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<NavigationItem> getNavigations() {
        return navigations;
    }

    public void setNavigations(List<NavigationItem> navigations) {
        this.navigations = navigations;
    }

    @Override
    public String toString() {
        return "PermissionNavigation{" + "permissions=" + permissions + ", navigations=" + navigations + '}';
    }

}
