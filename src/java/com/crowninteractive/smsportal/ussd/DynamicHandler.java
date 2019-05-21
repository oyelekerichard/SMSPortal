/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.ussd;

import com.crowninteractive.smsportal.dto.Data;
import com.crowninteractive.smsportal.dto.MtechRequest;
import com.crowninteractive.smsportal.dto.MtechResponse;
import com.crowninteractive.smsportal.dto.StaffDetail;
import com.crowninteractive.smsportal.model.MenuItem;
import com.crowninteractive.smsportal.service.MasterUSSDServiceImpl;
import com.crowninteractive.smsportal.service.UssdTxnServiceImpl;
import com.crowninteractive.smsportal.util.UtilMethods;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author Oluremi Adekanmbi
 * <oluremi.adekanmbi@etranzact.com>
 */
public class DynamicHandler implements ShortCodeInterface {

    private final MasterUSSDServiceImpl instance = MasterUSSDServiceImpl.getInstance();
    private final UssdTxnServiceImpl txnServiceImpl = UssdTxnServiceImpl.getInstance();
    private MenuItem menu = null;
    private final Map<String, Object> sessionData = new ConcurrentHashMap<String, Object>();
    private final USSDSessionManager manager = USSDSessionManager.getInstance();
    private USSDSession session = null;
    private static final Logger L = Logger.getLogger(DynamicHandler.class);

    @Override
    public String process(Data data) {
        StringBuilder sb = new StringBuilder();
        String process = "We could not process at the moment. Please try again later.";
        String reference = data.getSessionId() + "|" + new Date().getTime();
        txnServiceImpl.createTxn(data.getMsisdn(), data.getSessionId(), data.getMessage(), reference, data.getProvider());
        L.info(reference);
        if ("0".equals(data.getMessage())) {
            process = "You have opted to cancel your transaction. Thank you.";
            manager.destroySession(data.getSessionId());
        } else if ("N".equalsIgnoreCase(data.getMessage())) {
            //<editor-fold defaultstate="collapsed" desc="NEXT IMPLEMENTATION">
            USSDSession nextSession = manager.getSession(data.getSessionId());
            switch (nextSession.getPagePosition()) {
                case 0:
                    process = "";
                    nextSession.setPagePosition(nextSession.getPagePosition() + 1);
                    process += "";
                    break;
                case 1:
                    process = "";
                    nextSession.setPagePosition(nextSession.getPagePosition() + 1);
                    process += "";
                    break;
                default:
                    process = "";
                    process += "Invalid menu selection";
                    break;
            }
            //</editor-fold>
        } else if ("P".equalsIgnoreCase(data.getMessage())) {
            //<editor-fold defaultstate="collapsed" desc="PREVIOUS IMPLEMENTATION">
            USSDSession prevSession = manager.getSession(data.getSessionId());
            int page = prevSession.getPagePosition();
            switch (page) {
                case 0:
                    prevSession.setPagePosition(prevSession.getPagePosition() - 1);
                    sb.append("Please select a bank~");
                    sb.append("");
                    process = sb.toString();
                    break;
                case 1:
                    prevSession.setPagePosition(prevSession.getPagePosition() - 1);
                    sb.append("Please select a bank~");
                    sb.append("");
                    process = sb.toString();
                    break;
                case 2:
                    process = "";
                    prevSession.setPagePosition(prevSession.getPagePosition() - 1);
                    process += "";
                    break;
                default:
                    process += "Invalid menu selection";
                    break;
            }
            //</editor-fold>
        } else {
            //<editor-fold defaultstate="collapsed" desc="IMPLEMENTATION PART">
            menu = instance.findMenuWithIncoming(data.getMessage());
            if (menu == null) {
                String styledShortCode = MasterUSSDServiceImpl.getShortCodeStyle(data.getMessage());
                menu = instance.findMenuWithIncoming(styledShortCode);
            }
            USSDSession saved = manager.getSession(data.getSessionId());
            if (saved == null) {
                //<editor-fold defaultstate="collapsed" desc="SESSION IS NULL">
                session = new USSDSession(data.getMsisdn(), data.getSessionId());
                session.setProvider(data.getProvider());
                session.addTo(data.getMessage());
                session.getSessionData().put("sessionData", data.getRequest());
                L.info("Incoming message ::: " + data.getMessage());
                menu = instance.findMenuWithIncoming(data.getMessage());
                if (menu == null) {
                    String styledShortCode = UtilMethods.getShortCodeStyle(data.getMessage());
                    L.info(styledShortCode);
                    menu = instance.findMenuWithIncoming(styledShortCode);
                }

                if (menu == null) {
                    return "Unrecognised shortcode. Please confirm and dial again. Thank you.";
                }
                session.getSessionData().put("nextMenu", menu.getMenuItem());

                switch (menu.getReturnType()) {
                    case STATIC:
                        process = menu.getMenuMessage();
                        break;
                    case APPLICATION:
                        if (menu.isMakeServiceCallBefore()) {
                            DataInterface newInstance;
                            try {
                                newInstance = (DataInterface) Class.forName(menu.getServiceName()).newInstance();
                                session.setCurrentSection(menu.getServiceSection());
                                session = newInstance.process(session);
                                process = session.getResponse();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {

                        }
                        break;
                    case COMBINED:
                        process = "Combined Service and Skeleton message";
                        break;
                }
                manager.add(session);
                //</editor-fold>
            } else {
                //<editor-fold defaultstate="collapsed" desc="SESSION IS NOT NULL">
                session = manager.getSession(data.getSessionId());
                session.addTo(data.getMessage());
                menu = (MenuItem) session.getSessionData().get("nextMenu");
                L.info(menu);
                try {
                    session.getSessionData().put("nextMenu", menu.getMenuItem());
                } catch (NullPointerException e) {
                }
                //L.info(menu.getMenuItem().getId());
                if (menu.isMakeServiceCallBefore()) {
                    DataInterface newInstance;
                    try {
                        newInstance = (DataInterface) Class.forName(menu.getServiceName()).newInstance();
                        session.setCurrentSection(menu.getServiceSection());
                        session = newInstance.process(session);
                        process = session.getResponse();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    process = menu.getMenuMessage();
                }

                int indexOf = process.indexOf("Ref:");
                if (indexOf != -1) {
                    manager.destroySession(session.getSessionId());
                }
                indexOf = process.indexOf("Transaction");
                if (indexOf != -1) {
                    manager.destroySession(session.getSessionId());
                }
                indexOf = process.indexOf("successfully");
                if (indexOf != -1) {
                    manager.destroySession(session.getSessionId());
                }
                //</editor-fold>
            }

            L.info(process);
            //</editor-fold>
        }

        L.info(reference);
        if (process.length() > 200) {
            txnServiceImpl.updateTxn(reference, process.substring(0, 199));
        } else {
            txnServiceImpl.updateTxn(reference, process);
        }
        return process;
    }

    @Override
    public MtechResponse process(MtechRequest request) {
        MtechResponse resp = new MtechResponse();
        String process = "We could not process at the moment. Please try again later.";
        String sessionId = request.getSessionId().split(":")[1];
        if (request.getMessage() == null || request.getMessage().isEmpty()) {
            request.setMessage("*696*7#");
        }
        txnServiceImpl.createTxn(request.getMobile(), sessionId, request.getMessage(), request.getSessionId(), request.getOperator().toUpperCase());
        L.info(request.getSessionId());
        if ("0".equals(request.getMessage())) {
            process = "You have opted to cancel your transaction. Thank you.";
            manager.destroySession(sessionId);
        } else {
            //<editor-fold defaultstate="collapsed" desc="IMPLEMENTATION PART">

            USSDSession saved = manager.getSession(sessionId);
            if (saved == null) {
                //<editor-fold defaultstate="collapsed" desc="SESSION IS NULL">
                session = new USSDSession(request.getMobile(), sessionId);
                session.setProvider(request.getOperator().toUpperCase());
                session.addTo("*696*7#");
                session.getSessionData().put("sessionData", request.getSessionId());
                L.info("Incoming message ::: " + "*696*7#");
                menu = instance.findMenuWithIncoming("*696*7#");
                L.info("Menu found! " + "*696*7#");
                StaffDetail sd = UtilMethods.validatePhone(UtilMethods.msisdn(request.getMobile()));
                L.info(sd);
                session.getSessionData().put("staffDetail", sd);
                if (menu == null) {
                    String styledShortCode = UtilMethods.getShortCodeStyle(request.getMessage());
                    L.info(styledShortCode);
                    menu = instance.findMenuWithIncoming(styledShortCode);
                }

                if (menu == null) {
                    return new MtechResponse("Unrecognised shortcode. Please confirm and dial again. Thank you.", "Release", "");
                }
                session.getSessionData().put("nextMenu", menu.getMenuItem());

                switch (menu.getReturnType()) {
                    case STATIC:
                        process = menu.getMenuMessage();
                        break;
                    case APPLICATION:
                        if (menu.isMakeServiceCallBefore()) {
                            DataInterface newInstance;
                            try {
                                newInstance = (DataInterface) Class.forName(menu.getServiceName()).newInstance();
                                session.setCurrentSection(menu.getServiceSection());
                                session = newInstance.process(session);
                                process = session.getResponse();
                                L.info("Finished Page implementation!!!!");
                                L.info("Returning..... ");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {

                        }
                        break;
                    case COMBINED:
                        process = "Combined Service and Skeleton message";
                        break;
                }
                manager.add(session);
                //</editor-fold>
            } else {
                //<editor-fold defaultstate="collapsed" desc="SESSION IS NOT NULL">
                session = manager.getSession(sessionId);
                session.addTo(request.getMessage());
                menu = (MenuItem) session.getSessionData().get("nextMenu");
                L.info(menu);
                try {
                    session.getSessionData().put("nextMenu", menu.getMenuItem());
                } catch (NullPointerException e) {
                }
                //L.info(menu.getMenuItem().getId());
                if (menu.isMakeServiceCallBefore()) {
                    DataInterface newInstance;
                    try {
                        newInstance = (DataInterface) Class.forName(menu.getServiceName()).newInstance();
                        session.setCurrentSection(menu.getServiceSection());
                        session = newInstance.process(session);
                        process = session.getResponse();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    process = menu.getMenuMessage();
                }
                //</editor-fold>
            }

            L.info(process);
            //</editor-fold>
        }

        int indexOf = process.indexOf("Ref:");
        if (indexOf != -1) {
            resp.setClientState("Release");
            resp.setType("Release");
            manager.destroySession(session.getSessionId());
        } else {
            resp.setClientState("Response");
        }
        indexOf = process.indexOf("Transaction");
        if (indexOf != -1) {
            resp.setClientState("Release");
            resp.setType("Release");
            manager.destroySession(session.getSessionId());
        } else {
            resp.setClientState("Response");
        }
        indexOf = process.indexOf("uccess");
        if (indexOf != -1) {
            resp.setClientState("Release");
            resp.setType("Release");
            manager.destroySession(session.getSessionId());
        } else {
            resp.setClientState("Response");
        }
        L.info(request.getSessionId());
        try {
            StaffDetail sd = (StaffDetail) session.getSessionData().get("staffDetail");
            if (sd == null) {
                sd = new StaffDetail();
                sd.setName("");
                sd.setStaff_id("");
                sd.setDistricts("");
                sd.setDepartment("");
                sd.setStaff_code("");
                sd.setStatus("");
            }
            if (process.length() > 200) {
                if (sd != null) {
                    txnServiceImpl.updateTxn(request.getSessionId(), process.substring(0, 199), sd.getName(), sd.getStaff_id(), sd.getDistricts(), sd.getStaff_code(), sd.getStatus());
                } else {
                    txnServiceImpl.updateTxn(request.getSessionId(), process.substring(0, 199));
                }
            } else {
                if (sd != null) {
                    txnServiceImpl.updateTxn(request.getSessionId(), process, sd.getName(), sd.getStaff_id(), sd.getDistricts(), sd.getStaff_code(), sd.getStatus());
                } else {
                    txnServiceImpl.updateTxn(request.getSessionId(), process);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.setMessage(process);
        return resp;
    }
}
