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
 * @author adekanmbi
 */
public class DLRReportJob implements Runnable {

    private final ISWSSMSServiceImpl serviceImpl = ISWSSMSServiceImpl.getInstance();
    private final Logger L = Logger.getLogger(ISWSSMSServiceImpl.class);

    @Override
    public void run() {
        while (true) {
            L.info("Checking for Delivery Reports");
            try {
                List<BroadcastLog> findBLs = serviceImpl.findBLs();
                if (findBLs != null) {
                    L.info("Found " + findBLs.size() + " tickets waiting for updates");
                    for (BroadcastLog findBL : findBLs) {
                        serviceImpl.querySMSTicketStatus(findBL.getTicketId());
                    }
                    L.info("Done processing items.");
                }
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
