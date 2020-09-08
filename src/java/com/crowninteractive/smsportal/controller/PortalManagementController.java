/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.controller;

import com.crowninteractive.smsportal.dto.BaseResponse;
import com.crowninteractive.smsportal.dto.DeliveryReport;
import com.crowninteractive.smsportal.dto.StaffValidate;
import com.crowninteractive.smsportal.interswitch.service.ISWSSMSServiceImpl;
import com.crowninteractive.smsportal.model.UssdTransaction;
import com.crowninteractive.smsportal.model.dto.GenericCount;
import com.crowninteractive.smsportal.model.dto.SMSDetails;
import com.crowninteractive.smsportal.service.DownloadService;
import com.crowninteractive.smsportal.service.PortalManagementService;
import com.crowninteractive.smsportal.service.SMSServiceImpl;
import com.crowninteractive.smsportal.service.UssdTxnServiceImpl;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import com.google.gson.Gson;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author adekanmbi
 */
@Path("/portal/")
public class PortalManagementController {

    private final PortalManagementService PORTALSERVICE = PortalManagementService.getInstance();
    private final DownloadService DOWNLOADSERVICE = DownloadService.getInstance();
    private final UssdTxnServiceImpl TXNINSTANCE = UssdTxnServiceImpl.getInstance();
    private final SMSServiceImpl SMSINSTANCE = SMSServiceImpl.getInstance();
    private final ISWSSMSServiceImpl ISWINSTANCE = ISWSSMSServiceImpl.getInstance();
    private final Logger L = Logger.getLogger(PortalManagementController.class);
    private static final Gson GSON = new Gson();

    @Context
    private HttpServletRequest servletRequest;
    @Context
    private HttpServletResponse servletResponse;

    @GET
    @Path(value = "findNetworkProviders")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse findNetworkProviders() {
        return PORTALSERVICE.findNetworkProviders();
    }

    @GET
    @Path(value = "getSMSData/{start}/{end}/{page}/{size}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse getSMSData(
            @PathParam("start") long start,
            @PathParam("end") long end,
            @PathParam("page") int page,
            @PathParam("size") int size) {
        return PORTALSERVICE.getSMSData(start, end, page, size);
    }

    @GET
    @Path(value = "getSMSUnitsCount")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse getSMSUnitsCount() {
        return SMSINSTANCE.getSMSUnitsCount();
    }

    @GET
    @Path(value = "searchByCriterion/{criterion}/{message}/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse searchByCriterion(
            @PathParam("criterion") String criterion,
            @PathParam("message") String message,
            @PathParam("start") long start,
            @PathParam("end") long end) {
        return PORTALSERVICE.getDataByCriterion(criterion, message, start, end);
    }

    @GET
    @Path(value = "allSMSBySearchCriterion/{criterion}/{message}/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse allSMSBySearchCriterion(
            @PathParam("criterion") String criterion,
            @PathParam("message") String message,
            @PathParam("start") long start,
            @PathParam("end") long end) {
        return PORTALSERVICE.allSMSBySearchCriterion(criterion, message, start, end);
    }

    @GET
    @Path(value = "getUSSDDataByCriterion/{criterion}/{message}/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse getUSSDDataByCriterion(
            @PathParam("criterion") String criterion,
            @PathParam("message") String message,
            @PathParam("start") long start,
            @PathParam("end") long end) {
        return PORTALSERVICE.getUSSDDataByCriterion(criterion, message, start, end);
    }

    @POST
    @Path(value = "downloadSearchByCriterion")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void downloadSearchByCriterion(
            @FormParam("criterion") String criterion,
            @FormParam("message") String message,
            @FormParam("start") String start,
            @FormParam("end") String end) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("EE MMM d yyyy hh:mm:ss z");
            Date mStartDate = inputFormat.parse(start.substring(0, 28));
            Date mEndDate = inputFormat.parse(end.substring(0, 28));
            DOWNLOADSERVICE.downloadBySearchCriterion(criterion, message, mStartDate.getTime(), mEndDate.getTime(), servletResponse);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    @POST
    @Path(value = "downloadBySearchCriterionUSSD")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void downloadBySearchCriterionUSSD(
            @FormParam("criterion") String criterion,
            @FormParam("message") String message,
            @FormParam("start") String start,
            @FormParam("end") String end) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("EE MMM d yyyy hh:mm:ss z");
            Date mStartDate = inputFormat.parse(start.substring(0, 28));
            Date mEndDate = inputFormat.parse(end.substring(0, 28));
            DOWNLOADSERVICE.downloadBySearchCriterionUSSD(criterion, message, mStartDate.getTime(), mEndDate.getTime(), servletResponse);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    @POST
    @Path(value = "downloadSMSData")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void downloadSMSDataForm(
            @FormParam("start") String start,
            @FormParam("end") String end) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date mStartDate = inputFormat.parse(start);
            Date mEndDate = inputFormat.parse(end);
            DOWNLOADSERVICE.downloadAllByDate(mStartDate.getTime(), mEndDate.getTime(), servletResponse);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    @POST
    @Path(value = "downloadUSSDData")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void downloadUSSDDataForm(
            @FormParam("start") String start,
            @FormParam("end") String end) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date mStartDate = DateTimeUtil.getStartOfDate(inputFormat.parse(start));
            Date mEndDate = DateTimeUtil.getEndOfDate(inputFormat.parse(end));
            DOWNLOADSERVICE.downloadAllUSSDByDate(mStartDate.getTime(), mEndDate.getTime(), servletResponse);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    @GET
    @Path(value = "getSMSData/{rangeType}/{page}/{size}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse getSMSDataByRange(
            @PathParam("rangeType") String rangeType,
            @PathParam("page") int page,
            @PathParam("size") int size) {
        Date from, to;
        switch (rangeType) {
            case "TODAY":
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
            case "THIS_WEEK":
                from = DateTimeUtil.getStartOfWeek();
                to = DateTimeUtil.getEndOfWeek();
                break;
            case "THIS_MONTH":
                from = DateTimeUtil.getStartOfMonth();
                to = DateTimeUtil.getEndOfMonth();
                break;
            case "THIS_YEAR":
                from = DateTimeUtil.getStartOfYear(DateTimeUtil.getCurrentDate());
                to = DateTimeUtil.getEndOfYear(DateTimeUtil.getCurrentDate());
                break;
            case "YESTERDAY":
                from = DateTimeUtil.getStartOfYest();
                to = DateTimeUtil.getEndOfYest();
                break;
            default:
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
        }
        return PORTALSERVICE.getSMSData(from.getTime(), to.getTime(), page, size);
    }

    @GET
    @Path(value = "getSMSDataByKeyword/{keyword}/{rangeType}/{page}/{size}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse getSMSDataByKeywordByRange(
            @PathParam("keyword") String keyword,
            @PathParam("rangeType") String rangeType,
            @PathParam("page") int page,
            @PathParam("size") int size) {
        Date from, to;
        switch (rangeType) {
            case "TODAY":
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
            case "THIS_WEEK":
                from = DateTimeUtil.getStartOfWeek();
                to = DateTimeUtil.getEndOfWeek();
                break;
            case "THIS_MONTH":
                from = DateTimeUtil.getStartOfMonth();
                to = DateTimeUtil.getEndOfMonth();
                break;
            case "THIS_YEAR":
                from = DateTimeUtil.getStartOfYear(DateTimeUtil.getCurrentDate());
                to = DateTimeUtil.getEndOfYear(DateTimeUtil.getCurrentDate());
                break;
            case "YESTERDAY":
                from = DateTimeUtil.getStartOfYest();
                to = DateTimeUtil.getEndOfYest();
                break;
            default:
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
        }
        return PORTALSERVICE.getSMSDataByKeyword(keyword, from.getTime(), to.getTime(), page, size);
    }

    @GET
    @Path(value = "getSMSDataByKeyword/{keyword}/{start}/{end}/{page}/{size}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse getSMSDataByKeyword(
            @PathParam("keyword") String keyword,
            @PathParam("start") long start,
            @PathParam("end") long end,
            @PathParam("page") int page,
            @PathParam("size") int size) {
        return PORTALSERVICE.getSMSDataByKeyword(keyword, start, end, page, size);
    }

    @GET
    @Path(value = "reInitiateAction/{uniqueId}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse reInitiateAction(
            @PathParam("uniqueId") String uniqueId) {
        return PORTALSERVICE.reInitiateAction(uniqueId);
    }

    @GET
    @Path(value = "resendResponse/{uniqueId}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse resendResponse(
            @PathParam("uniqueId") String uniqueId) {
        return PORTALSERVICE.resendResponse(uniqueId);
    }

    @POST
    @Path(value = "sendCustomSMS")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse sendCustomSMS(SMSDetails sms) {
        return PORTALSERVICE.sendEKEDPSMS(sms);
    }

    @GET
    @Path(value = "verifyPhoneNumber/{phoneNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public StaffValidate verifyPhoneNumber(
            @PathParam("phoneNumber") String phoneNumber) {
        return PORTALSERVICE.verifyPhoneNumber(phoneNumber);
    }

    @GET
    @Path("/findDistinctTransaction/{rangeType}/{page}/{size}")
    @Produces({"application/json"})
    public String findDistinctTransaction(@PathParam(value = "rangeType") String rangeType, @PathParam(value = "page") int page,
            @PathParam(value = "size") int size) {
        Date from, to;
        switch (rangeType) {
            case "TODAY":
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
            case "THIS_WEEK":
                from = DateTimeUtil.getStartOfWeek();
                to = DateTimeUtil.getEndOfWeek();
                break;
            case "THIS_MONTH":
                from = DateTimeUtil.getStartOfMonth();
                to = DateTimeUtil.getEndOfMonth();
                break;
            case "THIS_YEAR":
                from = DateTimeUtil.getStartOfYear(DateTimeUtil.getCurrentDate());
                to = DateTimeUtil.getEndOfYear(DateTimeUtil.getCurrentDate());
                break;
            case "YESTERDAY":
                from = DateTimeUtil.getStartOfYest();
                to = DateTimeUtil.getEndOfYest();
                break;
            default:
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
        }
        List<UssdTransaction> distinctTransactions = TXNINSTANCE.findDistinctTransactions2(from, to, page, size);
        String retVal = GSON.toJson(distinctTransactions);
        return retVal;
    }

    @GET
    @Path("/findDistinctTransactionPageNSize/{page}/{size}")
    @Produces({"application/json"})
    public String findDistinctTransaction(
            @PathParam(value = "page") int page,
            @PathParam(value = "size") int size) {
        List<UssdTransaction> distinctTransactions = TXNINSTANCE.findDistinctTransactions(page, size);
        String retVal = GSON.toJson(distinctTransactions);
        return retVal;
    }

    @GET
    @Path("/findLogsBySessionId/{sessionId}")
    @Produces({"application/json"})
    public String findLogsBySessionId(
            @PathParam(value = "sessionId") String sessionId) {
        List<UssdTransaction> distinctTransactions = TXNINSTANCE.findTxnBySessionId(sessionId);
        String retVal = GSON.toJson(distinctTransactions);
        return retVal;
    }

    @GET
    @Path("/findDistinctTransaction/{from}/{to}")
    @Produces({"application/json"})
    public String findDistinctTransaction(@PathParam(value = "from") long start,
            @PathParam(value = "to") long end) {
        Date from = DateTimeUtil.getDateFor(start);
        Date to = DateTimeUtil.getDateFor(end);
        List<UssdTransaction> distinctTransactions = TXNINSTANCE.findDistinctTransactions(from, to);
        String retVal = GSON.toJson(distinctTransactions);
        return retVal;
    }

    @GET
    @Path("/findDistinctTransaction/{rangeType}")
    @Produces({"application/json"})
    public String findDistinctTransaction(@PathParam(value = "rangeType") String rangeType) {
        Date from, to;
        switch (rangeType) {
            case "TODAY":
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
            case "THIS_WEEK":
                from = DateTimeUtil.getStartOfWeek();
                to = DateTimeUtil.getEndOfWeek();
                break;
            case "THIS_MONTH":
                from = DateTimeUtil.getStartOfMonth();
                to = DateTimeUtil.getEndOfMonth();
                break;
            case "THIS_YEAR":
                from = DateTimeUtil.getStartOfYear(DateTimeUtil.getCurrentDate());
                to = DateTimeUtil.getEndOfYear(DateTimeUtil.getCurrentDate());
                break;
            case "YESTERDAY":
                from = DateTimeUtil.getStartOfYest();
                to = DateTimeUtil.getEndOfYest();
                break;
            default:
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
        }
        List<UssdTransaction> distinctTransactions = TXNINSTANCE.findDistinctTransactions(from, to);

        String retVal = GSON.toJson(distinctTransactions);
        return retVal;
    }

    @GET
    @Path("/findUniqueUSSDCountByRange/{rangeType}")
    @Produces({"application/json"})
    public String findUniqueUSSDCount(@PathParam(value = "rangeType") String rangeType) {
        Date from, to;
        switch (rangeType) {
            case "TODAY":
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
            case "THIS_WEEK":
                from = DateTimeUtil.getStartOfWeek();
                to = DateTimeUtil.getEndOfWeek();
                break;
            case "THIS_MONTH":
                from = DateTimeUtil.getStartOfMonth();
                to = DateTimeUtil.getEndOfMonth();
                break;
            case "THIS_YEAR":
                from = DateTimeUtil.getStartOfYear(DateTimeUtil.getCurrentDate());
                to = DateTimeUtil.getEndOfYear(DateTimeUtil.getCurrentDate());
                break;
            case "YESTERDAY":
                from = DateTimeUtil.getStartOfYest();
                to = DateTimeUtil.getEndOfYest();
                break;
            default:
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
        }
        List<GenericCount> findUniqueUSSDCount = TXNINSTANCE.findUniqueUSSDCount(from, to);
        String retVal = GSON.toJson(findUniqueUSSDCount);
        return retVal;
    }

    @GET
    @Path("/findUniqueUSSDCountByDate/{from}/{to}")
    @Produces({"application/json"})
    public String findUniqueUSSDCount(@PathParam(value = "from") long start,
            @PathParam(value = "to") long end) {
        Date from = DateTimeUtil.getDateFor(start);
        Date to = DateTimeUtil.getDateFor(end);
        List<GenericCount> findUniqueUSSDCount = TXNINSTANCE.findUniqueUSSDCount(from, to);
        String retVal = GSON.toJson(findUniqueUSSDCount);
        return retVal;
    }

    @GET
    @Path("/searchUSSDTxn/{searchString}")
    @Produces({"application/json"})
    public String searchUSSDTxn(
            @PathParam(value = "searchString") String searchString) {
        List<UssdTransaction> search = TXNINSTANCE.search(searchString);
        String retVal = GSON.toJson(search);
        return retVal;
    }

    @GET
    @Path("/getUSSDDashboardStats/{from}/{to}")
    @Produces({"application/json"})
    public String getUSSDDashboardStats(@PathParam(value = "from") long start,
            @PathParam(value = "to") long end) {
        Date from = DateTimeUtil.getStartOfDate(DateTimeUtil.getDateFor(start));
        Date to = DateTimeUtil.getDateFor(end);
        HashMap<String, Object> dashboardData = TXNINSTANCE.dashboardData(from, to);
        String retVal = GSON.toJson(dashboardData);
        return retVal;
    }

    @GET
    @Path("/getUSSDDashboardStatsByRange/{rangeType}")
    @Produces({"application/json"})
    public String getUSSDDashboardStatsByRange(@PathParam(value = "rangeType") String rangeType) {
        Date from, to;
        switch (rangeType) {
            case "TODAY":
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
            case "THIS_WEEK":
                from = DateTimeUtil.getStartOfWeek();
                to = DateTimeUtil.getEndOfWeek();
                break;
            case "THIS_MONTH":
                from = DateTimeUtil.getStartOfMonth();
                to = DateTimeUtil.getEndOfMonth();
                break;
            case "THIS_YEAR":
                from = DateTimeUtil.getStartOfYear(DateTimeUtil.getCurrentDate());
                to = DateTimeUtil.getEndOfYear(DateTimeUtil.getCurrentDate());
                break;
            case "YESTERDAY":
                from = DateTimeUtil.getStartOfYest();
                to = DateTimeUtil.getEndOfYest();
                break;
            default:
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
        }
        HashMap<String, Object> dashboardData = TXNINSTANCE.dashboardData(from, to);
        String retVal = GSON.toJson(dashboardData);
        return retVal;
    }

    @GET
    @Path(value = "getDeliveryReportStatsByRange/{rangeType}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DeliveryReport> getDeliveryReportStatsByRange(
            @PathParam("rangeType") String rangeType) {
        Date from, to;
        switch (rangeType) {
            case "TODAY":
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
            case "THIS_WEEK":
                from = DateTimeUtil.getStartOfWeek();
                to = DateTimeUtil.getEndOfWeek();
                break;
            case "THIS_MONTH":
                from = DateTimeUtil.getStartOfMonth();
                to = DateTimeUtil.getEndOfMonth();
                break;
            case "THIS_YEAR":
                from = DateTimeUtil.getStartOfYear(DateTimeUtil.getCurrentDate());
                to = DateTimeUtil.getEndOfYear(DateTimeUtil.getCurrentDate());
                break;
            case "YESTERDAY":
                from = DateTimeUtil.getStartOfYest();
                to = DateTimeUtil.getEndOfYest();
                break;
            default:
                from = DateTimeUtil.getStartOfToday();
                to = DateTimeUtil.getEndOfDate(DateTimeUtil.getCurrentDate());
                break;
        }
        return PORTALSERVICE.getCompositeDeliveryReportStatsByDate(from.getTime(), to.getTime());
    }

    @GET
    @Path(value = "getDeliveryReportStatsByDate/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DeliveryReport> getDeliveryReportStatsByDate(
            @PathParam("from") long from, @PathParam("to") long to) {
        return PORTALSERVICE.getCompositeDeliveryReportStatsByDate(from, to);
    }

    @GET
    @Path(value = "searchDeliveryReports/{searchCriterion}/{searchText}/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse searchDeliveryReports(@PathParam("searchCriterion") String searchCriterion, @PathParam("searchText") String searchText,
            @PathParam("from") long from, @PathParam("to") long to) {
        return PORTALSERVICE.findDeliveryReports(searchCriterion, searchText, from, to);
    }

    @GET
    @Path(value = "findBroadcastLogs")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse findBroadcastLogs() {
        return ISWINSTANCE.findBLs2();
    }
}
