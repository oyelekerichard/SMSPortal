/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.template;

import com.crowninteractive.smsportal.dto.StaffDetail;
import com.crowninteractive.smsportal.dto.WFM;
import com.crowninteractive.smsportal.dto.WFM2;
import com.crowninteractive.smsportal.model.UssdTransactionLog;
import com.crowninteractive.smsportal.service.UssdTxnServiceImpl;
import com.crowninteractive.smsportal.ussd.DataInterface;
import com.crowninteractive.smsportal.ussd.USSDSession;
import com.crowninteractive.smsportal.ussd.USSDSessionManager;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import com.crowninteractive.smsportal.util.UtilMethods;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author adekanmbi
 */
public class EkoUSSDImpl implements DataInterface {

    private static final Logger L = Logger.getLogger(EkoUSSDImpl.class);

    @Override
    public USSDSession process(USSDSession session) {
        StringBuilder sb;
        String page;

        switch (session.getCurrentSection().toUpperCase()) {
            case "START"://1
                //<editor-fold defaultstate="collapsed" desc="START">
                sb = new StringBuilder();
                sb.append("Welcome to EKEDP Online!").append("\n");
                sb.append("Select your option").append("\n");
                sb.append("1. Disconnection").append("\n");
                sb.append("2. Meter Reading").append("\n");
                sb.append("3. Track Ticket Id").append("\n");
                sb.append("4. Last Payment").append("\n");
                sb.append("5. Power Availability").append("\n");
                session.setResponse(sb.toString());
                //</editor-fold>
                break;
            case "PAGE_2"://2
                //<editor-fold defaultstate="collapsed" desc="PAGE 2">
                page = session.getQueue().get(1);
                switch (page) {
                    case "1":
                    case "3":
                        session.setResponse("Enter your ticket id");
                        break;
                    case "2":
                        session.setResponse("Enter your meter number");
                        break;
                    case "4":
                    case "5":
                        session.setResponse("Enter your account number");
                        break;
                    default:
                        session.setResponse("Invalid menu option chosen");
                        USSDSessionManager.getInstance().destroySession(session.getSessionId());
                        break;
                }
                //</editor-fold>
                break;
            case "PAGE_3"://3
                //<editor-fold defaultstate="collapsed" desc="PAGE 3">
                page = session.getQueue().get(1);
                switch (page) {
                    case "1":
                        sb = new StringBuilder();
                        sb.append("Select a ticket status").append("\n");
                        String wfmStatusForUSSD = UtilMethods.getWFMStatusForUSSD(session.getMsisdn(), session.getQueue().get(2));
                        if (wfmStatusForUSSD != null && !wfmStatusForUSSD.isEmpty()) {
                            sb.append(wfmStatusForUSSD);
                        } else {
                            sb = new StringBuilder();
                            sb.append("We could not fetch statuses at the moment. Please try again later. Thank you.");
                            USSDSessionManager.getInstance().destroySession(session.getSessionId());
                        }

                        session.setResponse(sb.toString());
                        break;
                    case "2":
                        session.setResponse("Enter meter reading");
                        break;
                    case "3": //Track
                        String trackTicketId = UtilMethods.trackTicketId(session.getQueue().get(2));
                        if (trackTicketId != null && !trackTicketId.isEmpty()) {
                            session.setResponse(trackTicketId);
                        } else {
                            session.setResponse("We could not fetch status at the moment. Please try again later. Thank you.");
                        }
                        createLogs(session, session.getResponse(), "TRACK_TICKET_ID");
                        USSDSessionManager.getInstance().destroySession(session.getSessionId());
                        break;
                    case "4": //Last Payment
                        String lastPayment = UtilMethods.getLastPayment(session.getQueue().get(2));
                        session.setResponse(lastPayment);
                        createLogs(session, session.getResponse(), "LAST_PAYMENT");
                        USSDSessionManager.getInstance().destroySession(session.getSessionId());
                        break;
                    case "5": //Last Payment
                        String powerAvailability = UtilMethods.getPowerAvailability(session.getQueue().get(2));
                        if (powerAvailability != null && !powerAvailability.isEmpty()) {
                            session.setResponse(powerAvailability);
                        } else {
                            session.setResponse("Power availability data not available for this account number. Powered by EKEDP.");
                        }
                        createLogs(session, session.getResponse(), "POWER_AVAILABILITY");
                        USSDSessionManager.getInstance().destroySession(session.getSessionId());
                        break;
                    default:
                        session.setResponse("Invalid menu option chosen.");
                        break;
                }
                //</editor-fold>
                break;
            case "PAGE_4"://4
                //<editor-fold defaultstate="collapsed" desc="PAGE 4">
                page = session.getQueue().get(1);
                switch (page) {
                    case "1":
                        sb = new StringBuilder();
                        List<WFM> wfmStatuses = UtilMethods.getWFMStatus(session.getMsisdn(), session.getQueue().get(2));
                        if (wfmStatuses.isEmpty()) {
                            sb.append("No status list attached to TicketId ").append(session.getQueue().get(2)).append(". Powered by EKEDP.").append("\n");
                        } else {
                            int statusIndex = Integer.parseInt(session.getQueue().get(3));
                            WFM wfm = wfmStatuses.get(statusIndex - 1);
                            WFM2 w = new WFM2();
                            w.setStatusToken(wfm.getToken().trim());
                            w.setStatusName(wfm.getName().trim());
                            w.setTicketId(Integer.parseInt(session.getQueue().get(2)));

                            String updateWFMStatus = UtilMethods.updateWFMStatus(session.getMsisdn(), w);
                            if (updateWFMStatus != null) {
                                session.setResponse(updateWFMStatus);
                            } else {
                                session.setResponse("Error processing your transaction. Please send Track." + session.getQueue().get(2) + " to 55999 to confirm status of transaction.");
                            }
                        }
                        createLogs(session, session.getResponse(), "DISCONNECTION");
                        USSDSessionManager.getInstance().destroySession(session.getSessionId());
                        break;
                    case "2":
                        String submitMeterReading = UtilMethods.submitMeterReading(session.getMsisdn(), session.getQueue().get(2), session.getQueue().get(3));
                        session.setResponse(submitMeterReading);
                        createLogs(session, session.getResponse(), "METER_READING");
                        USSDSessionManager.getInstance().destroySession(session.getSessionId());
                        break;
                    default:
                        session.setResponse("Invalid menu option chosen.");
                        USSDSessionManager.getInstance().destroySession(session.getSessionId());
                        break;
                }
                //</editor-fold>
                break;
        }
        return session;
    }

    public void createLogs(USSDSession session, String lastMessage, String actionType) {
        StaffDetail sd = (StaffDetail) session.getSessionData().get("staffDetail");
        if (sd == null) {
            sd = new StaffDetail();
            sd.setName("");
            sd.setStaff_id("");
            sd.setDistricts("");
            sd.setDepartment("");
            sd.setStaff_code("");
            sd.setStatus("");
            sd.setJob_title("");
        }
        UssdTransactionLog utl = new UssdTransactionLog();
        utl.setActionType(actionType);
        utl.setDepartment(sd.getDepartment());
        utl.setDistrict(sd.getDistricts());
        utl.setJobTitle(sd.getJob_title());
        utl.setMobileNo(session.getMsisdn());
        utl.setProvider(session.getProvider());
        utl.setRequestCode(session.getQueue().get(0));
        utl.setResponseMessage(lastMessage);
        utl.setShortCode(session.getQueue().get(0));
        utl.setStaffCode(sd.getStaff_code());
        utl.setStaffId(sd.getStaff_id());
        utl.setStaffName(sd.getName());
        utl.setStaffPhone(sd.getPhone());
        utl.setTransDate(DateTimeUtil.getCurrentDate());
        utl.setStatus(sd.getStatus());
        UssdTxnServiceImpl.getInstance().createTxnLog(utl);
    }
}
