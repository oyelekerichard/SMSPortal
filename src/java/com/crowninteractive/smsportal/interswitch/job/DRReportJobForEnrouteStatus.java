/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.interswitch.job;

import com.crowninteractive.smsportal.interswitch.service.ISWSSMSServiceImpl;
import com.crowninteractive.smsportal.model.BroadcastLog;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author richard.oyeleke
 */
public class DRReportJobForEnrouteStatus implements Runnable {

    private final ISWSSMSServiceImpl serviceImpl = ISWSSMSServiceImpl.getInstance();
    private final Logger L = Logger.getLogger(ISWSSMSServiceImpl.class);

    @Override
    public void run() {
        while (true) {
            L.info("Checking for Delivery Reports for Enroute Status");
            try {
                List<BroadcastLog> findBLsForEnrouteStatus = serviceImpl.findBLsEnrouteStatus();
                if (findBLsForEnrouteStatus != null) {
                    L.info("Found " + findBLsForEnrouteStatus.size() + " tickets waiting for updates");
                    for (BroadcastLog findBLEnroute : findBLsForEnrouteStatus) {
                        serviceImpl.querySMSTicketStatus(findBLEnroute.getTicketId());
                    }
                    L.info("Done processing items.");
                }
                Thread.sleep(15000); //To change body of generated methods, choose Tools | Templates.

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
