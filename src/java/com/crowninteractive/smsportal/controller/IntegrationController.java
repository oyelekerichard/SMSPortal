/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.controller;

import com.crowninteractive.smsportal.dto.BaseResponse;
import com.crowninteractive.smsportal.dto.Broadcast;
import com.crowninteractive.smsportal.dto.MtechRequest;
import com.crowninteractive.smsportal.dto.MtechResponse;
import com.crowninteractive.smsportal.service.PortalManagementService;
import com.crowninteractive.smsportal.service.SMSServiceImpl;
import com.crowninteractive.smsportal.ussd.DynamicHandler;
import com.crowninteractive.smsportal.util.CryptoUtil;
import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author adekanmbi
 */
@Path("/integration/")
public class IntegrationController {

    private final Logger L = Logger.getLogger(IntegrationController.class);

    @Context
    private HttpServletRequest servletRequest;
    @Context
    private HttpServletResponse servletResponse;
    private final Gson GSON = new Gson();
    private final DynamicHandler handler = new DynamicHandler();
    private final SMSServiceImpl SMSSERVICE = SMSServiceImpl.getInstance();
    private final PortalManagementService PMSERVICE = PortalManagementService.getInstance();

    @GET
    @Path(value = "processSMS")
    @Produces(MediaType.APPLICATION_JSON)
    public void processSMS(@QueryParam("msisdn") String msisdn,
            @QueryParam("scode") String scode,
            @QueryParam("text") String text,
            @QueryParam("adapter") String adapter) {
        L.info("=======================================================");
        SMSServiceImpl.getInstance().processor(msisdn, scode, text, adapter);
        L.info("=======================================================");
    }

    @GET
    @Path(value = "test")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse test() {
        return new BaseResponse();
    }

    @POST
    @Path(value = "ussd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String ussd(String message) {
        MtechRequest mtechRequest = GSON.fromJson(message, MtechRequest.class);
        L.info(mtechRequest);
        MtechResponse mtechResponse = handler.process(mtechRequest);
        L.info(mtechResponse);
        return GSON.toJson(mtechResponse);
    }

    @POST
    @Path(value = "doBroadcast")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse doBroadcast(Broadcast broadcast) {
        SMSSERVICE.doBroadcast(broadcast.getMsisdns(), broadcast.getMessage(), broadcast.getChannel());
        return new BaseResponse(broadcast.getMsisdns().size());
    }

    @POST
    @Path(value = "doBroadcastTest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse doBroadcastTest(Broadcast broadcast) {
        SMSSERVICE.doBroadcastTest(broadcast.getMsisdns(), broadcast.getMessage(), broadcast.getChannel());
        return new BaseResponse(broadcast.getMsisdns().size());
    }

    @GET
    @Path(value = "getLicenseKey/{appId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String test(@PathParam("appId") String appId) {
        return CryptoUtil.generateNewLicenceKey(appId);
    }

    @GET
    @Path(value = "readLogFile/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse readLogFile(@PathParam("date") String date) {
        L.info("Calling Service ....................................");
        return PMSERVICE.readLogFile(date);
    }

}
