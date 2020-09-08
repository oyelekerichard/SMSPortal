/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.job;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

/**
 * Web application lifecycle listener.
 *
 * @author adekanmbi
 */
public class StartupAndShutdownHook implements ServletContextListener {

    private final Logger L = Logger.getLogger(StartupAndShutdownHook.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==============================================");
        System.out.println("Initializing scaffold....");
        //DeliveryLogReader.start();
        System.out.println("==============================================");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("==============================================");
        //DeliveryLogReader.stop();
        System.out.println("Context destroyed!");
        System.out.println("==============================================");
    }
}
