/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.controller;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author oluremi.adekanmbi
 */
@ApplicationPath("web")
public class ApplicationConfig extends Application {

    private Set<Object> singletons = new HashSet<Object>();

    public ApplicationConfig() {
        singletons.add(new UserController());
        singletons.add(new PortalManagementController());
        singletons.add(new IntegrationController());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<Class<?>>();
        addRestResource(resources);
        return resources;
    }

    private void addRestResource(Set<Class<?>> resources) {
        resources.add(com.crowninteractive.smsportal.controller.UserController.class);
        resources.add(com.crowninteractive.smsportal.controller.IntegrationController.class);
        resources.add(com.crowninteractive.smsportal.controller.PortalManagementController.class);
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * re-generated by NetBeans REST support to populate given list with all
     * resources defined in the project.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.crowninteractive.smsportal.controller.IntegrationController.class);
        resources.add(com.crowninteractive.smsportal.controller.PortalManagementController.class);
        resources.add(com.crowninteractive.smsportal.controller.UserController.class);
    }
}