/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.job;

import com.crowninteractive.smsportal.service.PortalManagementService;
import org.apache.log4j.Logger;

/**
 *
 * @author adekanmbi
 */
public class DeliveryLogReader implements Runnable {

    private static final Logger L = Logger.getLogger(DeliveryLogReader.class);
    private static boolean isActive = false;
    private static final DeliveryLogReader TASK = new DeliveryLogReader();
    private static final long TIMEOUT = 60000;
    private final PortalManagementService PMSERVICE = PortalManagementService.getInstance();

    public static void start() {
        L.info("Starting DeliveryLogReader Job...!");
        isActive = true;
        ConcurrencyManager.execute(new Runnable() {
            @Override
            public void run() {
                while (isActive) {
                    try {
                        ConcurrencyManager.execute(TASK);
                        Thread.sleep(TIMEOUT);
                    } catch (Exception exc) {
                        L.warn("Executor in an illegal state. Perhaps it has been stopped.", exc);
                    }
                    isActive = false;
                }

                L.info("DeliveryLogReader Job stopped!");
            }
        });
    }

    public static void stop() {
        L.info("Stop flag raised for DeliveryLogReader Job.");
        isActive = false;
    }

    @Override
    public void run() {
        L.info("DeliveryLogReader running");
        try {
            PMSERVICE.readLogFile("2019-04-17");
        } catch (Exception exception) {
            L.error("Problems running DB cron. May be due to absent data!");
        }
    }
}
