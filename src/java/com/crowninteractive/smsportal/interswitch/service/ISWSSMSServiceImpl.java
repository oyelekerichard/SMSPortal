/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.interswitch.service;

import com.crowninteractive.smsportal.dto.BaseResponse;
import com.crowninteractive.smsportal.interswitch.dto.Account;
import com.crowninteractive.smsportal.interswitch.dto.BulkRequest;
import com.crowninteractive.smsportal.interswitch.dto.BulkResponse;
import com.crowninteractive.smsportal.interswitch.dto.DLR;
import com.crowninteractive.smsportal.interswitch.dto.LongRequest;
import com.crowninteractive.smsportal.interswitch.dto.LongResponse;
import com.crowninteractive.smsportal.interswitch.dto.LongResponseHolder;
import com.crowninteractive.smsportal.interswitch.dto.QueryDLR;
import com.crowninteractive.smsportal.interswitch.dto.QueryDLRResponse;
import com.crowninteractive.smsportal.interswitch.dto.SMS;
import com.crowninteractive.smsportal.interswitch.dto.SMSRequest;
import com.crowninteractive.smsportal.interswitch.dto.SMSResponse;
import com.crowninteractive.smsportal.model.BroadcastLog;
import com.crowninteractive.smsportal.model.Settings;
import com.crowninteractive.smsportal.service.DBAccessBean;
import com.crowninteractive.smsportal.util.Config;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import com.crowninteractive.smsportal.util.HttpUtil;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author adekanmbi
 */
public class ISWSSMSServiceImpl {

    private final static ISWSSMSServiceImpl INSTANCE = new ISWSSMSServiceImpl();
    private final DBAccessBean accessbean = new DBAccessBean("SMSPortalPU");
    private final Logger L = Logger.getLogger(ISWSSMSServiceImpl.class);

    private final Config CONFIG = Config.getInstance();
    private final Gson GSON = new Gson();

    private ISWSSMSServiceImpl() {
    }

    public static ISWSSMSServiceImpl getInstance() {
        return INSTANCE;
    }

    public Account getAccountDetails() {
        return new Account(CONFIG.getISWSystemId(), CONFIG.getISWPassword());
    }

    public SMSResponse sendSingleSMS(String msisdn, String message) {
        SMSResponse resp = null;
        Account account = getAccountDetails();
        SMS sms = new SMS(CONFIG.getISWSource());
        sms.setDest(msisdn);
        sms.setText(message);
        SMSRequest smsRequest = new SMSRequest(account, sms);
        String toJson = GSON.toJson(smsRequest);
        try {
            String response = HttpUtil.sendHttpsPost(CONFIG.getISWSingleSubmitURL(), toJson);
            resp = GSON.fromJson(response, SMSResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return resp;
    }

    public BulkResponse sendBulkSMS(List<String> msisdns, String message, String channel, Settings s) {
        BulkResponse bresp = null;
        int listSize = 0, currentLocation = 0, size = 90;

        List<String> internationalFormat = new ArrayList<>();
        for (String normal : msisdns) {
            internationalFormat.add(internationalize(normal.replaceAll(" ", "")));
        }

        if (internationalFormat.size() > size) {
            listSize = internationalFormat.size();
            BulkRequest breq;
            List<String> subList;
            while (currentLocation < listSize) {
                subList = internationalFormat.subList(currentLocation, (size + currentLocation > listSize ? listSize : size + currentLocation));
                breq = new BulkRequest();

                breq.setDestinations(subList);
                breq.setText(message);
                if (s != null) {
                    breq.setSrc(s.getCurrentValue());
                } else {
                    breq.setSrc(CONFIG.getISWSource());
                }

                breq.setAccount(getAccountDetails());
                String toJson = GSON.toJson(breq);
                L.info(toJson);
                try {
                    String sendHttpsPost = HttpUtil.sendHttpsPost(CONFIG.getISWMultipleSubmitURL(), toJson);
                    bresp = GSON.fromJson(sendHttpsPost, BulkResponse.class);

                    List<SMSResponse> responses = bresp.getResponses();
                    int counter = 0;
                    for (SMSResponse response : responses) {
                        BroadcastLog broadcastLog = new BroadcastLog(internationalFormat.get(counter),
                                message, channel, response.getStatus(),
                                DateTimeUtil.getCurrentDate(), DateTimeUtil.getCurrentDate().getTime() + "", response.getTicketId());
                        broadcastLog.setTicketStatus(response.getStatus());
                        broadcastLog.setProvider("INTERSWITCH");
                        if (s != null) {
                            broadcastLog.setHeader(s.getCurrentValue());
                        }
                        accessbean.create(broadcastLog);
                        counter++;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }

                currentLocation += size;
            }

        } else {
            BulkRequest breq = new BulkRequest();

            breq.setDestinations(internationalFormat);
            breq.setText(message);
            if (s != null) {
                breq.setSrc(s.getCurrentValue());
            } else {
                breq.setSrc(CONFIG.getISWSource());
            }
            breq.setAccount(getAccountDetails());
            String toJson = GSON.toJson(breq);

            try {
                String sendHttpsPost = HttpUtil.sendHttpsPost(CONFIG.getISWMultipleSubmitURL(), toJson);
                bresp = GSON.fromJson(sendHttpsPost, BulkResponse.class);

                List<SMSResponse> responses = bresp.getResponses();
                int counter = 0;
                for (SMSResponse response : responses) {
                    BroadcastLog broadcastLog = new BroadcastLog(internationalFormat.get(counter),
                            message, channel, response.getStatus(),
                            DateTimeUtil.getCurrentDate(), DateTimeUtil.getCurrentDate().getTime() + "", response.getTicketId());
                    broadcastLog.setTicketStatus(response.getStatus());
                    broadcastLog.setProvider("INTERSWITCH");
                    if (s != null) {
                        broadcastLog.setHeader(s.getCurrentValue());
                    }
                    accessbean.create(broadcastLog);
                    counter++;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        return bresp;
    }

    public LongResponseHolder sendBulkSMS2(List<String> msisdns, String message, String channel, Settings s) {
        LongResponseHolder bresp = null;
        LongRequest lReq = new LongRequest();
        String src;
        if (s != null) {
            src = s.getCurrentValue();
        } else {
            src = CONFIG.getISWSource();
        }

        List<SMS> smss = new ArrayList<>();
        SMS sms = null;
        for (String normal : msisdns) {
            sms = new SMS(internationalize(normal.replaceAll(" ", "")), src, message);
            smss.add(sms);
        }
        lReq.setAccount(getAccountDetails());
        lReq.getRequests().addAll(smss);
        String toJson = GSON.toJson(lReq);

        try {
            String sendHttpsPost = HttpUtil.sendHttpsPost(CONFIG.getISWLongSubmitURL(), toJson);
            bresp = GSON.fromJson(sendHttpsPost, LongResponseHolder.class);

            List<LongResponse> responses = bresp.getResponses();
            for (LongResponse response : responses) {
                List<SMSResponse> messageParts = response.getMessageParts();
                for (SMSResponse messagePart : messageParts) {
                    BroadcastLog broadcastLog = new BroadcastLog(msisdns.get(0),
                            message, channel, messagePart.getStatus(),
                            DateTimeUtil.getCurrentDate(), DateTimeUtil.getCurrentDate().getTime() + "", messagePart.getTicketId());
                    broadcastLog.setTicketStatus(messagePart.getStatus());
                    broadcastLog.setProvider("INTERSWITCH");
                    if (s != null) {
                        broadcastLog.setHeader(s.getCurrentValue());
                    }
                    accessbean.create(broadcastLog);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return bresp;
    }

    public QueryDLRResponse querySMSTicketStatus(String ticketId) {
        QueryDLRResponse qdlrr = null;
        QueryDLR qdlr = new QueryDLR();
        qdlr.setAccount(getAccountDetails());
        qdlr.getTicketIds().add(ticketId);
        String toJson = GSON.toJson(qdlr);
        try {
            String sendHttpsPost = HttpUtil.sendHttpsPost(CONFIG.getQueryDLRURL(), toJson);
            qdlrr = GSON.fromJson(sendHttpsPost, QueryDLRResponse.class);

            List<DLR> dlrs = qdlrr.getDlrs();
            for (DLR dlr : dlrs) {
                BroadcastLog bl = findBL(dlr.getTicketId());
                bl.setFinalStatus(dlr.isIsFinal());
                bl.setTicketStatus(dlr.getStatus());
                bl.setUpdated(DateTimeUtil.getCurrentDate());
                accessbean.merge(bl);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return qdlrr;
    }

    private String internationalize(String msisdn) {

        if (msisdn.startsWith("0")) {
            String substring = msisdn.substring(1);
            return "234" + substring;
        }
        if (msisdn.startsWith("7")) {
            return "234" + msisdn;
        }
        if (msisdn.startsWith("8")) {
            return "234" + msisdn;
        }
        if (msisdn.startsWith("9")) {
            return "234" + msisdn;
        }
        if (msisdn.startsWith("234")) {
            return msisdn;
        }
        if (msisdn.startsWith("+234")) {
            return msisdn.substring(1);
        }
        return msisdn;
    }

    public BroadcastLog findBL(String ticketId) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select * from broadcast_log2 where ticket_id = '" + ticketId + "' limit 1";
            Query query = em.createNativeQuery(sql, BroadcastLog.class);
            List<BroadcastLog> bls = (List<BroadcastLog>) query.getResultList();
            if (bls != null) {
                return bls.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public List<BroadcastLog> findBLs() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select * from broadcast_log2 b where b.ticket_status = 'ACCEPTED' and b.final_status = 0 limit 50;";
            Query query = em.createNativeQuery(sql, BroadcastLog.class);
            List<BroadcastLog> bls = (List<BroadcastLog>) query.getResultList();
            return bls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public BaseResponse findBLs2() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select * from broadcast_log2 b order by id desc limit 100";
            Query query = em.createNativeQuery(sql, BroadcastLog.class);
            List<BroadcastLog> bls = (List<BroadcastLog>) query.getResultList();
            if (bls != null) {
                return new BaseResponse(bls);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

}
