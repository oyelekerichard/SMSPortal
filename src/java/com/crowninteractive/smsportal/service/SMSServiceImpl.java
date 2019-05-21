/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.service;

import com.crowninteractive.smsportal.dto.BaseResponse;
import com.crowninteractive.smsportal.enums.MeterType;
import com.crowninteractive.smsportal.enums.Status;
import com.crowninteractive.smsportal.model.BroadcastLog;
import com.crowninteractive.smsportal.model.Registrations;
import com.crowninteractive.smsportal.model.Settings;
import com.crowninteractive.smsportal.parser.EkoDistribution;
import com.crowninteractive.smsportal.sms.SMS;
import com.crowninteractive.smsportal.sms.SMSProcessor;
import com.crowninteractive.smsportal.sms.SmsSender;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import com.crowninteractive.smsportal.websrv.EsbAccount;
import com.crowninteractive.smsportal.websrv.EsbAccount_Service;
import com.crowninteractive.smsportal.websrv.general.EsbGeneral;
import com.crowninteractive.smsportal.websrv.general.EsbGeneral_Service;
import com.crowninteractive.smsportal.websrv.sms.SmsModule;
import com.crowninteractive.smsportal.websrv.sms.SmsModule_Service;
import com.crowninteractive.smsportal.websrv.ucg.Endpoint;
import com.crowninteractive.smsportal.websrv.ucg.Endpoint_Service;
import com.crowninteractive.smsportal.websrv.wallet.EsbWallet;
import com.crowninteractive.smsportal.websrv.wallet.EsbWallet_Service;
import com.crowninteractive.smsportal.websrv.workforce.EsbWorkforce;
import com.crowninteractive.smsportal.websrv.workforce.EsbWorkforce_Service;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author Adekanmbi Oluremi adekanmbi.oluremi@gmail.com +2348098753155
 */
public class SMSServiceImpl {

    private final Logger L = Logger.getLogger(SMSServiceImpl.class);
    private final DBAccessBean accessbean = new DBAccessBean("SMSPortalPU");
    private final static SMSServiceImpl INSTANCE = new SMSServiceImpl();
    private static final String WSDL1 = "http://99.81.162.1:28080/ESB/EsbGeneral?wsdl";
    private static final String WSDL2 = "http://99.81.162.1:28080/ESB/EsbWorkforce?wsdl";
    private static final String WSDL3 = "http://99.81.162.1:28080/ESB/EsbWallet?wsdl";
    private static final String WSDL4 = "http://99.81.162.1:28080/ESB/EsbAccount?wsdl";
    private static final String WSDL5 = "http://99.81.162.1:28080/ESB/SmsModule?wsdl";
    private static final String WSDL6 = "http://99.81.162.1:28080/UCG/Voucher/Endpoint?wsdl";
    private final SMSProcessor sp = new SMSProcessor();
    private final EkoDistribution distribution = EkoDistribution.getInstance();
    private static final AccountManagementService ASERVICE = AccountManagementService.getInstance();
    private static EsbGeneral_Service generalService;
    private static EsbGeneral general;

    private static Endpoint endpoint;
    private static Endpoint_Service endpointService;

    private static EsbWorkforce esbWorkforce;
    private static EsbWorkforce_Service esbWorkforceService;

    private static EsbWallet esbWallet;
    private static EsbWallet_Service esbWalletService;

    private static EsbAccount esbAccount;
    private static EsbAccount_Service esbAccountService;

    private static SmsModule smsMod;
    private static SmsModule_Service smsModuleService;

    private SMS sms;
    String returnedMessage = "";

    static {
        try {
            generalService = new EsbGeneral_Service(new URL(WSDL1));
            general = generalService.getEsbGeneralPort();
            esbWorkforceService = new EsbWorkforce_Service(new URL(WSDL2));
            esbWorkforce = esbWorkforceService.getEsbWorkforcePort();
            esbWalletService = new EsbWallet_Service(new URL(WSDL3));
            esbWallet = esbWalletService.getEsbWalletPort();
            esbAccountService = new EsbAccount_Service(new URL(WSDL4));
            esbAccount = esbAccountService.getEsbAccountPort();
            endpointService = new Endpoint_Service(new URL(WSDL6));
            endpoint = endpointService.getEndpointPort();
            smsModuleService = new SmsModule_Service(new URL(WSDL5));
            smsMod = smsModuleService.getSmsModulePort();
        } catch (Exception malformedURLException) {
            malformedURLException.printStackTrace();
        }
    }

    public static SMSServiceImpl getInstance() {
        return INSTANCE;
    }

    public Registrations findRegistration(String phone, MeterType meterType) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String query = "SELECT s FROM Registrations s WHERE s.phone =:phone AND s.meterType =:meterType";
            TypedQuery<Registrations> tq = em.createQuery(query, Registrations.class);
            tq.setParameter("phone", phone);
            tq.setParameter("meterType", meterType);
            return tq.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Registrations createReg(Registrations sms) {
        return accessbean.merge(sms);
    }

    public void processor(String msisdn, String scode, String text, String adapter) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<BaseResponse> submit = executor.submit(new Callable<BaseResponse>() {
            @Override
            public BaseResponse call() throws Exception {
                EntityManager em = accessbean.getEmf().createEntityManager();
                Long start = (new Date()).getTime();
                String uniqueTransId = DateTimeUtil.getCurrentDate().getTime() + "";
                SMS sms = new SMS(msisdn, scode, text, adapter, uniqueTransId);
                returnedMessage = distribution.dispatchByKeyword(sms, general, esbWorkforce, esbWallet, esbAccount, smsMod, uniqueTransId);
                try {
                    if (returnedMessage.equals("DONOTSENDMESSAGE")) {
                        L.info("Do not send message command given. Not sending any message.....");
                    } else if (!returnedMessage.equals("")) {
                        SmsSender.send(sms.getMsisdn(), sms.getScode(), returnedMessage);
                    } else {
                        returnedMessage = "Unrecognised SMS command. Please check your command is correct and try again. Thank you.";
                        SmsSender.send(sms.getMsisdn(), sms.getScode(), returnedMessage);
                    }

                } catch (Exception exception) {
                    L.info("Exception! Could not send message!");
                } finally {
                    em.close();
                }
                Long end = (new Date()).getTime();
                L.info("Processing done!");
                L.info((new StringBuilder()).append("Response : ").append(returnedMessage).toString());
                Long timeTaken = end - start;
                L.info((new StringBuilder()).append("Time taken to process command : ").append(timeTaken).append("ms").toString());
                return new BaseResponse();
            }
        });
        executor.shutdown();
    }

    public void doBroadcast(List<String> msisdns, String message, String channel) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        String batchId = DateTimeUtil.getCurrentDate().getTime() + "";
        for (String msisdn : msisdns) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Settings setting = ASERVICE.findSetting("SEND_SMS");
                        SmsSender.send(msisdn, setting.getCurrentValue(), message);
                        accessbean.create(new BroadcastLog(msisdn, message, channel, Status.MESSAGE_SENT, batchId));
                    } catch (Exception ex) {
                        L.info("Exception! Could not send message!");
                        accessbean.create(new BroadcastLog(msisdn, message, channel, Status.MESSAGE_NOT_SENT, batchId));
                    }
                }
            });
        }
        executorService.shutdown();
    }

    public void doBroadcastTest(List<String> msisdns, String message, String channel) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        String batchId = DateTimeUtil.getCurrentDate().getTime() + "";
        for (String msisdn : msisdns) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        L.info("Sending SMS to " + msisdn + "!");
                        L.info("Batch ID is " + batchId);
                    } catch (Exception ex) {
                        L.info("Exception! Could not send message!");
                        accessbean.create(new BroadcastLog(msisdn, message, channel, Status.MESSAGE_NOT_SENT, batchId));
                    }
                }
            });
        }
        executorService.shutdown();
    }

}
