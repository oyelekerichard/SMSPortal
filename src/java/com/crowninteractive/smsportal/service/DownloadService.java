/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.service;

import com.crowninteractive.smsportal.dto.BaseResponse;
import com.crowninteractive.smsportal.model.UssdTransaction;
import com.crowninteractive.smsportal.model.dto.SMSDetails;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import com.crowninteractive.smsportal.util.ResponseCodes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

/**
 *
 * @author adekanmbi
 */
public class DownloadService {

    private final DBAccessBean accessbean;
    private static DownloadService INSTANCE = new DownloadService();
    private final org.apache.log4j.Logger L = org.apache.log4j.Logger.getLogger(AccountManagementService.class);
    private final PortalManagementService PORTALSERVICE = PortalManagementService.getInstance();
    private final UssdTxnServiceImpl USSDSERVICE = UssdTxnServiceImpl.getInstance();

    public static DownloadService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DownloadService();
        }
        return INSTANCE;
    }

    private DownloadService() {
        accessbean = new DBAccessBean("SMSPortalPU");
    }

    public <T extends Object> byte[] isExcel(List<T> objects, String title, Class exclusion) {

        XSSFWorkbook workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {

            XSSFSheet sheet = workbook.createSheet(title);
            XSSFRow Headrow = null;
            int top = 0;
            int first = 1;
            for (T object : objects) {
                int nInx = 0;
                if (top == 0) {
                    Headrow = sheet.createRow(0);
                }
                XSSFRow row = sheet.createRow(first);

                for (Field declaredField : object.getClass().getDeclaredFields()) {
                    if (!declaredField.isAnnotationPresent(exclusion)) {
                        if (top == 0) {
                            XSSFCell customerIdCell = Headrow.createCell(nInx);
                            customerIdCell.setCellValue(declaredField.getName().toUpperCase());
                        }
                        XSSFCell customerIdCell = row.createCell(nInx);
                        declaredField.setAccessible(true);
                        customerIdCell.setCellValue(declaredField.get(object) != null ? declaredField.get(object).toString() : " ");
                        nInx++;
                    }
                }
                top++;
                first++;
            }
            workbook.write(bos);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return bos.toByteArray();

    }

    public BaseResponse downloadAllByDate(long startDate, long endDate, HttpServletResponse response) throws IOException {
        try {
            List<SMSDetails> smsData = PORTALSERVICE.getSMSData(startDate, endDate);
            if (smsData != null && !smsData.isEmpty()) {
                byte[] content = isExcel(smsData, "SMSDownload_" + DateTimeUtil.getShortDate(new Date()), SMSDetails[].class);
                OutputStream os = response.getOutputStream();
                response.setContentType("application/vnd.ms-excel");
                String filename = "SMSDownload_" + DateTimeUtil.getShortDate("yyyy_MM_dd", new Date()) + ".xlsx";
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                os.write(content);
                os.close();
                return new BaseResponse();
            }
            return new BaseResponse(ResponseCodes.EMPTY_RECORDS, ResponseCodes.getDefaultMessageFor(ResponseCodes.EMPTY_RECORDS));
        } catch (Exception e) {
            return new BaseResponse(ResponseCodes.SERVICE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.SERVICE_ERROR));
        }
    }

    public BaseResponse downloadAllUSSDByDate(long startDate, long endDate, HttpServletResponse response) throws IOException {
        try {
            List<UssdTransaction> ussdData = USSDSERVICE.findDistinctTransactions3(DateTimeUtil.getDateFor(startDate), DateTimeUtil.getDateFor(endDate));
            if (ussdData != null && !ussdData.isEmpty()) {
                byte[] content = isExcel(ussdData, "USSDDownload_" + DateTimeUtil.getShortDate(new Date()), SMSDetails[].class);
                OutputStream os = response.getOutputStream();
                response.setContentType("application/vnd.ms-excel");
                String filename = "USSDDownload_" + DateTimeUtil.getShortDate("yyyy_MM_dd", new Date()) + ".xlsx";
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                os.write(content);
                os.close();
                return new BaseResponse();
            }
            return new BaseResponse(ResponseCodes.EMPTY_RECORDS, ResponseCodes.getDefaultMessageFor(ResponseCodes.EMPTY_RECORDS));
        } catch (Exception e) {
            return new BaseResponse(ResponseCodes.SERVICE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.SERVICE_ERROR));
        }
    }

    public BaseResponse downloadBySearchCriterion(String criterion, String searchText, long startDate, long endDate, HttpServletResponse response) throws IOException {
        try {
            List<SMSDetails> smsData = (List<SMSDetails>) PORTALSERVICE.getDataByCriterion(criterion, searchText, startDate, endDate).getObj();
            if (smsData != null && !smsData.isEmpty()) {
                byte[] content = isExcel(smsData, "SMSDownload", SMSDetails[].class);
                OutputStream os = response.getOutputStream();
                response.setContentType("application/vnd.ms-excel");
                String filename = "SMSDownload_" + DateTimeUtil.getShortDate("yyyy_MM_dd", new Date()) + ".xlsx";
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                os.write(content);
                os.close();
                return new BaseResponse();
            }
            return new BaseResponse(ResponseCodes.EMPTY_RECORDS, ResponseCodes.getDefaultMessageFor(ResponseCodes.EMPTY_RECORDS));
        } catch (Exception e) {
            return new BaseResponse(ResponseCodes.SERVICE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.SERVICE_ERROR));
        }
    }

    public BaseResponse downloadBySearchCriterionUSSD(String criterion, String searchText, long startDate, long endDate, HttpServletResponse response) throws IOException {
        try {
            List<SMSDetails> smsData = (List<SMSDetails>) PORTALSERVICE.getUSSDDataByCriterion(criterion, searchText, startDate, endDate).getObj();
            if (smsData != null && !smsData.isEmpty()) {
                byte[] content = isExcel(smsData, "USSDDownload", SMSDetails[].class);
                OutputStream os = response.getOutputStream();
                response.setContentType("application/vnd.ms-excel");
                String filename = "USSDDownload_" + DateTimeUtil.getShortDate("yyyy_MM_dd", new Date()) + ".xlsx";
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                os.write(content);
                os.close();
                return new BaseResponse();
            }
            return new BaseResponse(ResponseCodes.EMPTY_RECORDS, ResponseCodes.getDefaultMessageFor(ResponseCodes.EMPTY_RECORDS));
        } catch (Exception e) {
            return new BaseResponse(ResponseCodes.SERVICE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.SERVICE_ERROR));
        }
    }

}
