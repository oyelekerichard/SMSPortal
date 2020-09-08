/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.service;

import com.crowninteractive.smsportal.dto.BaseResponse;
import com.crowninteractive.smsportal.dto.DeliveryReport;
import com.crowninteractive.smsportal.dto.EMCC;
import com.crowninteractive.smsportal.dto.EMCCResponse;
import com.crowninteractive.smsportal.dto.FeederStatusResponse;
import com.crowninteractive.smsportal.dto.NameValuePair;
import com.crowninteractive.smsportal.dto.StaffDetail;
import com.crowninteractive.smsportal.dto.StaffValidate;
import com.crowninteractive.smsportal.dto.StaffValidateError;
import com.crowninteractive.smsportal.dto.UCG;
import com.crowninteractive.smsportal.dto.WFM;
import com.crowninteractive.smsportal.dto.WFMResponse;
import com.crowninteractive.smsportal.model.SIncoming;
import com.crowninteractive.smsportal.model.SMSDeliveryLog;
import com.crowninteractive.smsportal.model.SOutgoing;
import com.crowninteractive.smsportal.model.Settings;
import com.crowninteractive.smsportal.model.UssdTransactionLog;
import com.crowninteractive.smsportal.model.dto.DeliveryCount;
import com.crowninteractive.smsportal.model.dto.SMSDetails;
import com.crowninteractive.smsportal.util.Config;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import com.crowninteractive.smsportal.util.HttpUtil;
import com.crowninteractive.smsportal.util.ResponseCodes;
import com.crowninteractive.smsportal.util.UtilMethods;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author adekanmbi
 */
public class PortalManagementService {

    private final DBAccessBean accessbean;
    private static PortalManagementService INSTANCE = new PortalManagementService();
    private static final AccountManagementService ASERVICE = AccountManagementService.getInstance();
    private final Logger L = Logger.getLogger(PortalManagementService.class);
    private static final Config CONFIG = Config.getInstance();

    public static PortalManagementService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PortalManagementService();
        }
        return INSTANCE;
    }

    private PortalManagementService() {
        accessbean = new DBAccessBean("SMSPortalPU");
    }

    public SOutgoing findOutgoing(String uniqueId) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String query = "SELECT * FROM SOutgoing s where s.uniqueId ='" + uniqueId + "'";
            Query createQuery = em.createNativeQuery(query, SOutgoing.class);
            return (SOutgoing) createQuery.getSingleResult();
        } finally {
            em.close();
        }
    }

    public BaseResponse getSMSData(long start, long end, int page, int size) {
        HashMap<String, Object> retData = new HashMap<>();
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select i.id, i.msisdn as 'msisdn', i.scode as 'scode', i.text as 'incoming', "
                    + " i.recieved as 'incoming_date', "
                    + " o.text as 'outgoing', i.uniqueId as 'uniqueId',"
                    + " o.staff_id as 'staff_id', o.staff_name  as 'staff_name', o.district  as 'district',"
                    + " o.sent as 'outgoing_date' from SIncoming i, SOutgoing o "
                    + "where "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'AC Power Goes%' and i.text not like 'External%' "
                    + "and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.uniqueId = o.uniqueId and i.msisdn = o.msisdn and i.recieved between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "' order by i.id desc";
            Query query = em.createNativeQuery(sql, SMSDetails.class);

            page = (page - 1) < 0 ? 0 : (page - 1);

            query.setFirstResult(page * size);
            query.setMaxResults(size);
            List<SMSDetails> smsdetails = (List<SMSDetails>) query.getResultList();
            retData.put("data", smsdetails);
            sql = "select i.id, i.msisdn as 'msisdn', i.scode as 'scode', i.text as 'incoming', "
                    + " i.recieved as 'incoming_date', "
                    + " o.text as 'outgoing', i.uniqueId as 'uniqueId',"
                    + " o.staff_id as 'staff_id', o.staff_name  as 'staff_name', o.district  as 'district',"
                    + " o.sent as 'outgoing_date' from SIncoming i, SOutgoing o "
                    + "where "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'AC Power Goes%' and i.text not like 'External%' "
                    + "and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.uniqueId = o.uniqueId and i.msisdn = o.msisdn and i.recieved between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "' order by i.id desc";
            query = em.createNativeQuery(sql, SMSDetails.class);
            smsdetails = (List<SMSDetails>) query.getResultList();
            int five = 0, seven = 0;
            for (SMSDetails smsdetail : smsdetails) {
                switch (smsdetail.getScode()) {
                    case "55999":
                        five++;
                        break;
                    case "7827":
                        seven++;
                        break;
                }
            }
            retData.put("five", five);
            retData.put("seven", seven);
            sql = "select count(*) from SIncoming i, SOutgoing o "
                    + "where "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.text not like 'AC Power Goes%' and i.text not like 'External%' and "
                    + "i.uniqueId = o.uniqueId and i.msisdn = o.msisdn and i.recieved between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "' order by i.id desc";
            query = em.createNativeQuery(sql);
            List listResult = query.getResultList();
            Long x = (Long) listResult.get(0);
            retData.put("total", x);
            retData.put("page", page);
            retData.put("nosOfPages", x / size);
            retData.put("size", size);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return new BaseResponse(retData);
    }

    public BaseResponse getSMSDataByKeyword(String keyword, long start, long end, int page, int size) {
        HashMap<String, Object> retData = new HashMap<>();
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select i.id, i.msisdn as 'msisdn', i.scode as 'scode', i.text as 'incoming', "
                    + "i.recieved as 'incoming_date', "
                    + "o.text as 'outgoing', i.uniqueId as 'uniqueId',"
                    + " o.staff_id as 'staff_id', o.staff_name  as 'staff_name', o.district  as 'district',"
                    + "o.sent as 'outgoing_date' from SIncoming i, SOutgoing o "
                    + "where i.text like '%" + keyword + "%' and "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.text not like 'AC Power Goes%' and i.text not like 'External%' and "
                    + "i.uniqueId = o.uniqueId and i.msisdn = o.msisdn and i.recieved between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "' order by i.id desc";
            Query query = em.createNativeQuery(sql, SMSDetails.class);
            page = (page - 1) < 0 ? 0 : (page - 1);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            List<SMSDetails> smsdetails = (List<SMSDetails>) query.getResultList();
            retData.put("data", smsdetails);
            sql = "select i.id, i.msisdn as 'msisdn', i.scode as 'scode', i.text as 'incoming', "
                    + " i.recieved as 'incoming_date', "
                    + " o.text as 'outgoing', i.uniqueId as 'uniqueId',"
                    + " o.staff_id as 'staff_id', o.staff_name  as 'staff_name', o.district  as 'district',"
                    + " o.sent as 'outgoing_date' from SIncoming i, SOutgoing o "
                    + "where i.text like '%" + keyword + "%' and "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.text not like 'AC Power Goes%' and i.text not like 'External%' and "
                    + "i.uniqueId = o.uniqueId and i.msisdn = o.msisdn and i.recieved between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "' order by i.id desc";
            query = em.createNativeQuery(sql, SMSDetails.class);
            smsdetails = (List<SMSDetails>) query.getResultList();
            int five = 0, seven = 0;
            for (SMSDetails smsdetail : smsdetails) {
                switch (smsdetail.getScode()) {
                    case "55999":
                        five++;
                        break;
                    case "7827":
                        seven++;
                        break;
                }
            }
            retData.put("five", five);
            retData.put("seven", seven);
            sql = "select count(*) from SIncoming i, SOutgoing o "
                    + "where i.text like '%" + keyword + "%' and "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.text not like 'AC Power Goes%' and i.text not like 'External%' and "
                    + "i.uniqueId = o.uniqueId and i.msisdn = o.msisdn and i.recieved between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "' order by i.id desc";
            query = em.createNativeQuery(sql);;
            List listResult = query.getResultList();
            Long x = (Long) listResult.get(0);
            retData.put("total", x);
            retData.put("page", page);
            retData.put("nosOfPages", x / size);
            retData.put("size", size);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return new BaseResponse(retData);
    }

    public List<SMSDetails> getSMSData(long start, long end) {
        HashMap<String, Object> retData = new HashMap<>();
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select i.id, i.msisdn as 'msisdn', i.scode as 'scode', i.text as 'incoming', "
                    + " i.recieved as 'incoming_date', "
                    + " o.text as 'outgoing', i.uniqueId as 'uniqueId',"
                    + " o.staff_id as 'staff_id', o.staff_name  as 'staff_name', o.district  as 'district',"
                    + " o.sent as 'outgoing_date' from SIncoming i, SOutgoing o "
                    + "where "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.text not like 'AC Power Goes%' and i.text not like 'External%' and "
                    + "i.uniqueId = o.uniqueId and i.msisdn = o.msisdn and i.recieved between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "' order by i.id desc";
            Query query = em.createNativeQuery(sql, SMSDetails.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public BaseResponse reInitiateAction(String uniqueId) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select i.id, i.msisdn as 'msisdn', i.scode as 'scode', i.text as 'incoming', "
                    + " i.recieved as 'incoming_date', "
                    + " o.text as 'outgoing', i.uniqueId as 'uniqueId',"
                    + " o.sent as 'outgoing_date' from SIncoming i, SOutgoing o "
                    + "where "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.text not like 'AC Power Goes%' and i.text not like 'External%' and "
                    + "i.uniqueId = o.uniqueId and i.msisdn = o.msisdn and i.uniqueId = '" + uniqueId + "'";
            Query query = em.createNativeQuery(sql, SMSDetails.class);
            List<SMSDetails> smsdetails = (List<SMSDetails>) query.getResultList();
            L.info("Size of data returned from fetching with uniqueId " + smsdetails.size());
            if (!smsdetails.isEmpty()) {
                SMSDetails sms = smsdetails.get(0);
                if (sms.getIncoming().toUpperCase().startsWith("WFM")) {
                    return processWFMRequest(sms.getMsisdn(), sms.getIncoming(), uniqueId);
                } else if (sms.getIncoming().toUpperCase().startsWith("METER")) {
                    return processMeterReadingRequest(sms.getMsisdn(), sms.getIncoming(), uniqueId);
                } else if (sms.getIncoming().toUpperCase().startsWith("Payment")) {
                    return processPaymentRequest(sms.getMsisdn(), sms.getIncoming(), uniqueId);
                } else if (sms.getIncoming().toUpperCase().startsWith("#")) {
                    return (BaseResponse) doCheckAvailability(sms.getIncoming());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public BaseResponse findNetworkProviders() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            List<NameValuePair> nvps = new ArrayList<>();
            String sql = "select * from sms_settings where identifier = 'PROVIDER' limit 1";
            Query query = em.createNativeQuery(sql, Settings.class);
            List<Settings> smsdetails = (List<Settings>) query.getResultList();
            if (smsdetails != null) {

                Settings settings = smsdetails.get(0);
                String[] split = settings.getCurrentValue().split(",");
                for (String string : split) {
                    nvps.add(new NameValuePair(string.trim(), string.trim()));
                }
            }
            return new BaseResponse(nvps);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public BaseResponse doCheckAvailability(String incoming) {
        String accountNumber = incoming.split("\\.")[1];
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder(CONFIG.getEMCCFeederStatus());
        sb.append(accountNumber);
        String retVal2 = "";
        FeederStatusResponse fs = null;
        try {
            retVal2 = HttpUtil.sendGet(sb.toString());
            System.out.println(retVal2);
            fs = gson.fromJson(retVal2, FeederStatusResponse.class);
            System.out.println(fs);
            System.out.println("===============================================================================");
            sb = new StringBuilder();
            sb.append("Dear ").append(accountNumber).append(" your feeder ").append(fs.getObject().getLowVoltageFeederId().getName().trim());
            sb.append(" coming from ").append(fs.getObject().getInjectionSubstationId().getName().trim()).append(" Injection Substation");
            sb.append(" is currently ").append(fs.getObject().getStatus()).append(". Last updated on ").append(fs.getObject().getUpdated()).append(". Powered by EKEDP.");
            System.out.println(sb.toString());
            System.out.println("===============================================================================");
        } catch (Exception e) {
            return null;
        }
        return new BaseResponse(fs.getObject());
    }

    public StaffValidate verifyPhoneNumber(String msisdn) {
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder(CONFIG.getWFMValidateURL());
        sb.append("0").append(msisdn);
        String retVal2 = "", returnMessage = "";
        StaffValidate sv = null;
        try {
            retVal2 = HttpUtil.sendGet(sb.toString());
            L.info(retVal2);
            sv = gson.fromJson(retVal2, StaffValidate.class);
        } catch (Exception e) {
            StaffValidateError sve = gson.fromJson(retVal2, StaffValidateError.class);
            sv = new StaffValidate();
            sv.setSuccess(sve.isSuccess());
        }
        return sv;
    }

    public BaseResponse processWFMRequest(String msisdn, String incomingText, String uniqueId) {
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder(CONFIG.getWFMValidateURL());
        sb.append("0").append(msisdn);
        String retVal2, returnMessage = "";
        try {
            retVal2 = HttpUtil.sendGet(sb.toString());
            L.info(retVal2);
            StaffValidate sv = null;
            try {
                sv = new Gson().fromJson(retVal2, StaffValidate.class);
            } catch (JsonSyntaxException jsonSyntaxException) {
                StaffValidateError sve = new Gson().fromJson(retVal2, StaffValidateError.class);
                sv = new StaffValidate();
                sv.setSuccess(sve.isSuccess());
            }

            if (sv.isSuccess()) {
                String wfm[] = incomingText.split("\\.");
                switch (wfm.length) {
                    case 1:
                        returnMessage = "Wrong syntax. Allowed syntax are WFM.TicketId and WFM.TicketId.Status";
                    case 2://WFM.TicketId
                        sb = new StringBuilder(CONFIG.getWFMStatusList());
                        sb.append("/").append(wfm[1].trim());
                        sb.append("/").append("0").append(msisdn);
                        String message = HttpUtil.sendGet(sb.toString());
                        WFMResponse wresp = gson.fromJson(message, WFMResponse.class);
                        List<WFM> object = wresp.getObject();
                        if (object.isEmpty()) {
                            returnMessage = "No status list attached to TicketId " + wfm[1] + ". Powered by EKEDP.";
                        } else {
                            sb = new StringBuilder("Send WFM." + wfm[1] + ". ");
                            int counter = 1;
                            for (WFM w : object) {
                                sb.append(counter).append("=").append(w.getName()).append(", ");
                                counter++;
                            }
                            returnMessage = sb.toString();
                        }
                    case 3://WFM.TicketId.Status
                        sb = new StringBuilder(CONFIG.getWFMStatusList());
                        sb.append("/").append(wfm[1].trim());
                        sb.append("/").append(msisdn);
                        String respw = HttpUtil.sendGet(sb.toString());
                        WFMResponse wResp = gson.fromJson(respw, WFMResponse.class);
                        List<WFM> wfms = wResp.getObject();
                        if (wfms.isEmpty()) {
                            returnMessage = "No status list attached to TicketId " + wfm[1] + ". Powered by EKEDP.";
                        } else {
                            WFM myWfm = wfms.get(Integer.parseInt(wfm[2]) - 1);
                            WFM w = new WFM();
                            w.setStatusToken(myWfm.getToken().trim());
                            w.setStatusName(myWfm.getName().trim());
                            w.setTicketId(Integer.parseInt(wfm[1].trim()));
                            sb = new StringBuilder(CONFIG.getWFMUpdateStatus());
                            sb.append("?phone=").append("0").append(msisdn);
                            String sendPost = HttpUtil.sendPost(sb.toString(), gson.toJson(w));
                            WFMResponse wfmResp = gson.fromJson(sendPost, WFMResponse.class);
                            List<WFM> obj = wfmResp.getObject();
                            if (obj.isEmpty()) {
                                if (wfmResp.getDesc().startsWith("System")) {
                                    returnMessage = "Please retry this " + incomingText + " request again with all spaces removed. Thank you.";
                                } else if (wfmResp.getDesc().startsWith("Invalid")) {
                                    returnMessage = "This phone number is attached to more than one User. Please contact your system administrator. Thank you.";
                                } else if (wfmResp.getDesc().startsWith("Successful")) {
                                    returnMessage = "Ticket #" + wfm[1].trim() + " has been updated to " + myWfm.getName().trim() + " status. Powered by EKEDP.";
                                } else {
                                    returnMessage = wfmResp.getDesc() + ". Powered by EKEDP.";
                                }
                            } else {
                                WFM w3 = obj.get(0);
                                sb = new StringBuilder();
                                sb.append("Payment Order Id: ").append(w3.getOrderId()).append(" for ").append(w3.getChargeName()).append(" has been generated for ").append(w3.getBillingAccount());
                                sb.append(" at ").append(w3.getAddress().replaceAll("\\+", " ")).append(" and the charge is N").append(w3.getDebtOnAccount());
                                returnMessage = sb.toString();
                            }
                        }

                        break;
                }
            } else {
                returnMessage = "Invalid Staff Phone Number. Powered by EKEDP";
            }
            L.info(returnMessage);
            SOutgoing outgoing = findOutgoing(uniqueId);
            outgoing.setText(returnMessage);
            accessbean.merge(outgoing);
            return new BaseResponse(returnMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse(ResponseCodes.OTHER_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.OTHER_ERROR));
        }
    }

    public BaseResponse processMeterReadingRequest(String msisdn, String incomingText, String uniqueId) {
        Gson gson = new Gson();
        String returnMessage;
        String meterNumber = incomingText.split("\\.")[2];
        String meterReading = incomingText.split("\\.")[3];
        L.info("Incoming Staff Phone ::: " + msisdn);
        StringBuilder sb2 = new StringBuilder(CONFIG.getWFMValidateURL());

        //sb2.append("0").append(phone);
        sb2.append("0").append(msisdn);
        L.info(sb2.toString());
        try {
            String retVal;
            retVal = HttpUtil.sendGet(sb2.toString());
            L.info("Meter Number ::: " + meterNumber);
            L.info("Meter Reading ::: " + meterReading);
            L.info(retVal);
            StaffValidate sv = new Gson().fromJson(retVal, StaffValidate.class);
            L.info(sv.toString());

            if (sv.isSuccess()) {
                StaffDetail sd = sv.getData()[0];
                L.info("Starting EMCC call....");
                sb2 = new StringBuilder(CONFIG.getSubmitMeterReadingURL());;
                EMCC emcc = new EMCC();
                emcc.setMeterNumber(meterNumber);
                emcc.setMeterReading(meterReading);
                emcc.setDistrict(sd.getDistricts());
                emcc.setPhone(msisdn);
                emcc.setName(sv.getData()[0].getName());
                L.info(sb2.toString());
                retVal = HttpUtil.sendPost(sb2.toString(), gson.toJson(emcc));
                L.info(retVal);
                EMCCResponse eResp = new Gson().fromJson(retVal, EMCCResponse.class);
                returnMessage = eResp.getDesc() + ". Powered by EKEDP";
            } else {
                returnMessage = "Not found. Invalid Staff Phone Number. Powered by EKEDP";
            }
            L.info(returnMessage);
            L.info("=====================Finishing Staff Section=============================");
        } catch (Exception ex) {
            ex.printStackTrace();
            returnMessage = "Invalid Staff Id. Powered by EKEDP";
            L.info(returnMessage);
            L.info("=====================Finishing Staff Section Exception Block=============================");
        }
        SOutgoing outgoing = findOutgoing(uniqueId);
        outgoing.setText(returnMessage);
        accessbean.merge(outgoing);
        return new BaseResponse(returnMessage);
    }

    public BaseResponse processPaymentRequest(String msisdn, String incomingText, String uniqueId) {
        String returnMessage;

        try {
            String accountNumber = incomingText.split("\\.")[1];
            L.info("Incoming Staff Phone ::: " + msisdn);
            StringBuilder sb2 = new StringBuilder(CONFIG.getWFMValidateURL());
            sb2.append("0").append(msisdn);
            L.info(sb2.toString());
            String retVal;
            retVal = HttpUtil.sendGet(sb2.toString());

            L.info(retVal);
            StaffValidate sv = new Gson().fromJson(retVal, StaffValidate.class);
            L.info(sv.toString());

            if (sv.isSuccess()) {
                StaffDetail sd = sv.getData()[0];
                sb2 = new StringBuilder(CONFIG.getUCGLastPaymet());
                sb2.append(accountNumber);
                retVal = HttpUtil.sendGet(sb2.toString());
                L.info(retVal);
                UCG ucg = new Gson().fromJson(retVal, UCG.class);
                L.info(ucg);
                if (ucg.getEntity() == null) {
                    returnMessage = "You do not have any current bill for this period. Powered by EKEDP.";
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("The last payment for ");
                    sb.append(ucg.getEntity().getSecondaryUserId());
                    sb.append(" is NGN");
                    sb.append(ucg.getEntity().getAmount());
                    sb.append(" on ");
                    sb.append(DateTimeUtil.getDateFor(ucg.getEntity().getConfirmationTime()));
                    sb.append(". TxRef : ").append(ucg.getEntity().getId());
                    sb.append(". Powered by EKEDP.");
                    returnMessage = sb.toString();
                }
            } else {
                returnMessage = "Not found. Invalid Staff Phone Number. Powered by EKEDP";
            }
            L.info(returnMessage);
            L.info("=====================Finishing Staff Section=============================");
        } catch (Exception ex) {
            ex.printStackTrace();
            returnMessage = "Invalid Staff Id. Powered by EKEDP";
            L.info(returnMessage);
            L.info("=====================Finishing Staff Section Exception Block=============================");
        }
        SOutgoing outgoing = findOutgoing(uniqueId);
        outgoing.setText(returnMessage);
        accessbean.merge(outgoing);
        return new BaseResponse(returnMessage);
    }

    public BaseResponse resendResponse(String uniqueId) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select i.id, i.msisdn as 'msisdn', i.scode as 'scode', i.text as 'incoming', "
                    + " i.recieved as 'incoming_date', "
                    + " o.text as 'outgoing', i.uniqueId as 'uniqueId',"
                    + " o.sent as 'outgoing_date' from SIncoming i, SOutgoing o "
                    + "where "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.text not like 'AC Power Goes%' and i.text not like 'External%' and "
                    + "i.uniqueId = o.uniqueId and i.uniqueId = '" + uniqueId + "'";
            Query query = em.createNativeQuery(sql, SMSDetails.class);
            List<SMSDetails> smsdetails = (List<SMSDetails>) query.getResultList();

            if (!smsdetails.isEmpty()) {
                SMSDetails sms = smsdetails.get(0);
                String returnMessage = postResponse(sms.getMsisdn(), sms.getOutgoing());
                return new BaseResponse(returnMessage);
            }
        } finally {
            em.close();
        }
        return null;
    }

    private String postResponse(String msisdn, String response) {
        try {
            StringBuilder sb = new StringBuilder("http://localhost:28080");
            sb.append("/adapter/sendsms/");
            sb.append("?destination=").append(URLEncoder.encode(msisdn));
            sb.append("&source=").append(URLEncoder.encode("55999"));
            sb.append("&text=").append(URLEncoder.encode(response));
            sb.append("&smscid=").append(URLEncoder.encode("mtech"));
            String sendGet = HttpUtil.sendGet(sb.toString());
            return sendGet;
        } catch (Exception ex) {
            return null;
        }
    }

    public BaseResponse sendEKEDPSMS(SMSDetails sms) {
        String reply = postEKEDPResponse(sms.getMsisdn(), sms.getIncoming());
        if (reply != null) {
            return new BaseResponse();
        } else {
            return new BaseResponse(3000, "There was an issue sending your SMS.");
        }
    }

    private String postEKEDPResponse(String msisdn, String response) {
        try {
            StringBuilder sb = new StringBuilder("http://localhost:28080");
            sb.append("/adapter/sendsms/");
            sb.append("?destination=").append(URLEncoder.encode(msisdn));
            sb.append("&source=").append(URLEncoder.encode("EKEDP"));
            sb.append("&text=").append(URLEncoder.encode(response));
            sb.append("&smscid=").append(URLEncoder.encode("crownbulk"));
            String sendGet = HttpUtil.sendGet(sb.toString());
            return sendGet;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<SMSDetails> getDataByMSISDN(String msisdn, long start, long end) {
        List<SMSDetails> smsdetails = null;
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select i.id, i.msisdn as 'msisdn', i.scode as 'scode', i.text as 'incoming', "
                    + " i.recieved as 'incoming_date', "
                    + " o.text as 'outgoing', i.uniqueId as 'uniqueId',"
                    + " o.staff_id as 'staff_id', o.staff_name  as 'staff_name', o.district  as 'district',"
                    + " o.sent as 'outgoing_date' from SIncoming i, SOutgoing o "
                    + "where "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.text not like 'AC Power Goes%' and i.text not like 'External%' and "
                    + "i.uniqueId = o.uniqueId and i.msisdn = o.msisdn and i.msisdn like '%" + msisdn + "%'"
                    + " and i.recieved between '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "';";
            L.info(sql);
            Query query = em.createNativeQuery(sql, SMSDetails.class);
            query.setFirstResult(0);
            query.setMaxResults(1000);
            smsdetails = (List<SMSDetails>) query.getResultList();
        } finally {
            em.close();
        }
        return smsdetails;
    }

    public List<SMSDetails> getDataByIncomingMessage(String incomingMessage, long start, long end) {
        List<SMSDetails> smsdetails = null;
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select i.id, i.msisdn as 'msisdn', i.scode as 'scode', i.text as 'incoming', "
                    + " i.recieved as 'incoming_date', "
                    + " o.text as 'outgoing', i.uniqueId as 'uniqueId',"
                    + " o.staff_id as 'staff_id', o.staff_name  as 'staff_name', o.district  as 'district',"
                    + " o.sent as 'outgoing_date' from SIncoming i, SOutgoing o "
                    + "where "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.text not like 'AC Power Goes%' and i.text not like 'External%' and "
                    + "i.uniqueId = o.uniqueId and i.msisdn = o.msisdn and i.text like '%" + incomingMessage + "%'"
                    + " and i.recieved between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "'";
            Query query = em.createNativeQuery(sql, SMSDetails.class);
            query.setFirstResult(0);
            query.setMaxResults(1000);
            smsdetails = (List<SMSDetails>) query.getResultList();
        } finally {
            em.close();
        }
        return smsdetails;
    }

    public List<SMSDetails> getDataByResponseMessage(String responseMessage, long start, long end) {
        List<SMSDetails> smsdetails = null;
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select i.id, i.msisdn as 'msisdn', i.scode as 'scode', i.text as 'incoming', "
                    + " i.recieved as 'incoming_date', "
                    + " o.text as 'outgoing', i.uniqueId as 'uniqueId',"
                    + " o.staff_id as 'staff_id', o.staff_name  as 'staff_name', o.district  as 'district',"
                    + " o.sent as 'outgoing_date' from SIncoming i, SOutgoing o "
                    + "where "
                    + "o.text != 'DONOTSENDMESSAGE' and o.text != 'No available record' and i.text not like 'DO%' and i.text not like '#%' and "
                    + "i.text not like 'AC Power Goes%' and i.text not like 'External%' and "
                    + "i.uniqueId = o.uniqueId and i.msisdn = o.msisdn and o.text like '%" + responseMessage + "%'"
                    + " and i.recieved between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "'";
            Query query = em.createNativeQuery(sql, SMSDetails.class);
            query.setFirstResult(0);
            query.setMaxResults(1000);
            smsdetails = (List<SMSDetails>) query.getResultList();
        } finally {
            em.close();
        }
        return smsdetails;
    }

    public BaseResponse getDataByCriterion(String criterion, String searchMessage, long start, long end) {
        List<SMSDetails> retVals = new ArrayList<>();
        switch (criterion.toUpperCase()) {
            case "CUSTOMER_PHONE":
                retVals = getDataByMSISDN(searchMessage, start, end);
                break;
            case "MESSAGE":
                retVals = getDataByIncomingMessage(searchMessage, start, end);
                break;
            case "RESPONSE":
                retVals = getDataByResponseMessage(searchMessage, start, end);
                break;
        }
        return new BaseResponse(retVals);
    }

    public BaseResponse allSMSBySearchCriterion(String criterion, String searchMessage, long start, long end) {
        List<SIncoming> retVals = new ArrayList<>();
        switch (criterion.toUpperCase()) {
            case "CUSTOMER_PHONE":
                retVals = getAllSMSByMSISDN(searchMessage, start, end);
                break;
            case "MESSAGE":
                retVals = getAllSMSByIncomingMessage(searchMessage, start, end);
                break;
        }
        return new BaseResponse(retVals);
    }

    public List<SIncoming> getAllSMSByMSISDN(String msisdn, long start, long end) {
        List<SIncoming> smsdetails = null;
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select * from SIncoming i where i.msisdn = '" + msisdn + "'"
                    + " and i.recieved between '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "';";
            L.info(sql);
            Query query = em.createNativeQuery(sql, SIncoming.class);
            query.setFirstResult(0);
            query.setMaxResults(200);
            smsdetails = (List<SIncoming>) query.getResultList();
        } finally {
            em.close();
        }
        return smsdetails;
    }

    public List<SIncoming> getAllSMSByIncomingMessage(String message, long start, long end) {
        List<SIncoming> smsdetails = null;
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select * from SIncoming i where i.text like '%" + message + "%'"
                    + " and i.recieved between '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "';";
            L.info(sql);
            Query query = em.createNativeQuery(sql, SIncoming.class);
            query.setFirstResult(0);
            query.setMaxResults(200);
            smsdetails = (List<SIncoming>) query.getResultList();
        } finally {
            em.close();
        }
        return smsdetails;
    }

    public List<UssdTransactionLog> getUSSDDataByMSISDN(String msisdn, long start, long end) {
        List<UssdTransactionLog> smsdetails = null;
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String msisdnToInternational = UtilMethods.msisdnToInternational(msisdn);
            String sql = "SELECT * FROM ussd_transaction_log u where "
                    + "u.mobile_no = '" + msisdnToInternational + "' and u.trans_date between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "'";
            Query query = em.createNativeQuery(sql, UssdTransactionLog.class);
            query.setFirstResult(0);
            query.setMaxResults(1000);
            smsdetails = (List<UssdTransactionLog>) query.getResultList();
        } finally {
            em.close();
        }
        return smsdetails;
    }

    public List<UssdTransactionLog> getUSSDDataByResponseMessage(String searchString, long start, long end) {
        List<UssdTransactionLog> smsdetails = null;
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "SELECT * FROM ussd_transaction_log u where u.response_message like "
                    + "'%" + searchString + "%' and u.trans_date between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd HH:mm:ss", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "'";

            Query query = em.createNativeQuery(sql, UssdTransactionLog.class);
            query.setFirstResult(0);
            query.setMaxResults(1000);
            smsdetails = (List<UssdTransactionLog>) query.getResultList();
        } finally {
            em.close();
        }
        return smsdetails;
    }

    public BaseResponse getUSSDDataByCriterion(String criterion, String searchMessage, long start, long end) {
        List<UssdTransactionLog> retVals = new ArrayList<>();
        switch (criterion.toUpperCase()) {
            case "CUSTOMER_PHONE":
                retVals = getUSSDDataByMSISDN(searchMessage, start, end);
                break;
            case "RESPONSE":
                retVals = getUSSDDataByResponseMessage(searchMessage, start, end);
                break;
        }
        return new BaseResponse(retVals);
    }

    public BaseResponse readLogFile(String date) {
        L.info("Reading log file for " + date);
        BaseResponse resp = null;
        try {
            String lineItem = "";
            String[] i = {};
            Scanner input = new Scanner(System.in);
            Settings setting = ASERVICE.findSetting("LOG_LOCATION");
            File file = new File(setting.getCurrentValue() + date);
            input = new Scanner(file);
            SMSDeliveryLog s = null;
            while (input.hasNextLine()) {
                String line = input.nextLine();
                if (line.contains("[stdout] (MessageReceiver)") && line.contains("text")) {
                    lineItem = line.substring(47, line.length() - 1);
                    L.info(lineItem);
                    String[] split = lineItem.split(",");
                    s = new SMSDeliveryLog();
                    for (String x : split) {
                        i = x.split("=");
                        //<editor-fold defaultstate="collapsed" desc="PACKAGING OBJECT">
                        switch (i[0].trim()) {
                            case "id":
                                s.setMessageId(i[1]);
                                break;
                            case "text":
                                s.setText(i[1]);
                                break;
                            case "sub":
                                s.setSub(i[1]);
                                break;
                            case "dest":
                                s.setDest(i[1]);
                                break;
                            case "err":
                                s.setErr(i[1]);
                                break;
                            case "dlvrd":
                                s.setDlvrd(i[1]);
                                break;
                            case "src":
                                s.setSrc(i[1]);
                                break;
                            case "date":
                                String j[] = date.split("\\-");
                                Date createDate = DateTimeUtil.createDate(Integer.valueOf(j[0]), Integer.valueOf(j[1]), Integer.valueOf(j[2]));
                                s.setDeliveryDate(createDate);
                                break;
                            case "stat":
                                s.setStat(i[1]);
                                break;
                        }
                        //</editor-fold>
                    }
                    L.info(s);
                    createDeliveryReport(s);
                }
            }
            L.info("Done!!!!!!");
            input.close();
            resp = new BaseResponse();
        } catch (Exception ex) {
            ex.printStackTrace();
            resp = new BaseResponse(ResponseCodes.OTHER_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.OTHER_ERROR));
        }
        return resp;
    }

    public BaseResponse createDeliveryReport(SMSDeliveryLog log) {
        accessbean.create(log);
        return new BaseResponse();
    }

    public BaseResponse findDeliveryReports(String criterion, String searchMessage, long start, long end) {
        switch (criterion) {
            case "CUSTOMER_PHONE":
                List<SMSDeliveryLog> deliveryReportsByPhone = getDeliveryReportsByPhone(searchMessage, start, end);
                return new BaseResponse(deliveryReportsByPhone);
        }
        return null;
    }

    public List<SMSDeliveryLog> getDeliveryReportsByPhone(String searchString, long start, long end) {
        List<SMSDeliveryLog> smsdetails = null;
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "SELECT * FROM sms_delivery_log u where u.src like "
                    + "'%" + searchString + "%' and u.dest != '7827' and u.delivery_date between '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "'";

            Query query = em.createNativeQuery(sql, SMSDeliveryLog.class);
            query.setFirstResult(0);
            query.setMaxResults(1000);
            smsdetails = (List<SMSDeliveryLog>) query.getResultList();
        } finally {
            em.close();
        }
        return smsdetails;
    }

    public List<DeliveryCount> getDeliveryReportStatsByDate(long start, long end) {
        List<DeliveryCount> smsdetails = null;
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select distinct(stat) as 'stat', count(*) as 'count', id, delivery_date as 'name' from sms_delivery_log s where s.dest != '7827' and "
                    + "s.delivery_date between  '"
                    + DateTimeUtil.getShortDate("yyyy-MM-dd", DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start)))
                    + "' and '" + DateTimeUtil.getShortDate("yyyy-MM-dd", DateTimeUtil.getEndOfDate(DateTimeUtil.getDateFor(end))) + "' group by delivery_date, stat";

            Query query = em.createNativeQuery(sql, DeliveryCount.class);
            smsdetails = (List<DeliveryCount>) query.getResultList();
        } finally {
            em.close();
        }
        return smsdetails;
    }

    public List<DeliveryReport> getCompositeDeliveryReportStatsByDate(long start, long end) {

        List<DeliveryReport> datas = new ArrayList<DeliveryReport>();
        List<DeliveryCount> mtxs = getDeliveryReportStatsByDate(start, end);
        L.info("Size returned by initial query ::: " + mtxs.size());
        try {
            DeliveryReport m = null;
            if (!mtxs.isEmpty()) {
                for (DeliveryCount mtx : mtxs) {
                    L.info(mtx);
                    if (datas.isEmpty()) {
                        m = new DeliveryReport(mtx.getName());
                        datas.add(m);
                    }

                    int indexOf = datas.indexOf(new DeliveryReport(mtx.getName()));
                    if (indexOf == -1) {
                        m = new DeliveryReport(mtx.getName());
                        datas.add(m);
                    } else {
                        m = datas.get(indexOf);
                    }
                    if (mtx.getStat().equalsIgnoreCase("DELIVRD")) {
                        m.setDelivrd(mtx.getCount() + "");
                    } else if (mtx.getStat().equalsIgnoreCase("EXPIRED")) {
                        m.setExpired(mtx.getCount() + "");
                    } else if (mtx.getStat().equalsIgnoreCase("UNDELIV")) {
                        m.setUndeliv(mtx.getCount() + "");
                    } else if (mtx.getStat().equalsIgnoreCase("UNKNOWN")) {
                        m.setUnknown(mtx.getCount() + "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }
}
