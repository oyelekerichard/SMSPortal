/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adekanmbi
 */
public class Config extends Properties {

    //this is an example of a Singleton class
    private static Config INSTANCE;

    private void loadProps() throws IOException {
        super.load(new FileInputStream("/usr/share/smsportal/config.properties"));
    }

    private Config() {
    }

    public static Config getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new Config();
                INSTANCE.loadProps();

            } catch (Exception ex) {
                INSTANCE = null;
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return INSTANCE;
    }

    public String getESBGeneralURL() {
        return getProperty("esb.general");
    }

    public String getESBWorkForceURL() {
        return getProperty("esb.work.force");
    }

    public String getESBWalletURL() {
        return getProperty("esb.wallet");
    }

    public String getESBAccountURL() {
        return getProperty("esb.account");
    }

    public String getESBSMSModuleURL() {
        return getProperty("esb.sms.module");
    }

    public String getUCGVoucherURL() {
        return getProperty("ucg.voucher");
    }

    public String getWFMStatusList() {
        return getProperty("wfm.status.list");
    }

    public String getWFMUpdateStatus() {
        return getProperty("wfm.update.status");
    }

    public String getSubmitMeterReadingURL() {
        return getProperty("emcc.submit.meter.reading");
    }

    public String getWFMValidateURL() {
        return getProperty("wfm.validate");
    }

    public String getUCGLastPaymet() {
        return getProperty("ucg.last.payment");
    }

    public String getEMCCFeederStatus() {
        return getProperty("wfm.validate");
    }

    public String getEMCCCurrentSchedule() {
        return getProperty("ucg.last.payment");
    }

    public String getEMCCUpdatePhoneNumber() {
        return getProperty("emcc.update.phone.number");
    }

    public String getEMCCRemovePhoneNumber() {
        return getProperty("emcc.remove.phone.number");
    }

    public String getEMCCAlsd() {
        return getProperty("emcc.alsd");
    }
}
