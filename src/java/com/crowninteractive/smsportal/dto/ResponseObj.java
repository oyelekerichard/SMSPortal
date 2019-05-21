/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

import com.crowninteractive.smsportal.model.Permission;
import com.crowninteractive.smsportal.model.Role;
import com.crowninteractive.smsportal.model.User;
import java.util.List;

/**
 *
 * @author CROWN-STAFF
 */
public class ResponseObj {

    private int start, size, users, roles, channels, permissions;
    private List<User> userdata;
    private List<Role> roledata;
    private List<Permission> permissiondata;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public int getRoles() {
        return roles;
    }

    public void setRoles(int roles) {
        this.roles = roles;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public List<User> getUserdata() {
        return userdata;
    }

    public void setUserdata(List<User> userdata) {
        this.userdata = userdata;
    }

    public List<Role> getRoledata() {
        return roledata;
    }

    public void setRoledata(List<Role> roledata) {
        this.roledata = roledata;
    }

    public List<Permission> getPermissiondata() {
        return permissiondata;
    }

    public void setPermissiondata(List<Permission> permissiondata) {
        this.permissiondata = permissiondata;
    }

}
