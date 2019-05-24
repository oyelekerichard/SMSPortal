/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.util;

import com.crowninteractive.smsportal.dto.EMCC;
import com.crowninteractive.smsportal.dto.EMCCResponse;
import com.crowninteractive.smsportal.dto.FeederStatus;
import com.crowninteractive.smsportal.dto.FeederStatusResponse;
import com.crowninteractive.smsportal.dto.PowerScheduleResponse;
import com.crowninteractive.smsportal.dto.StaffDetail;
import com.crowninteractive.smsportal.dto.StaffValidate;
import com.crowninteractive.smsportal.dto.UCG;
import com.crowninteractive.smsportal.dto.WFM;
import com.crowninteractive.smsportal.dto.WFM2;
import com.crowninteractive.smsportal.dto.WFM3;
import com.crowninteractive.smsportal.dto.WFMResponse;
import com.crowninteractive.smsportal.dto.WFMResponse2;
import com.crowninteractive.smsportal.sms.SMS;
import com.crowninteractive.smsportal.sms.SMSProcessor;
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
import com.crowninteractive.smsportal.websrv.workforce.WorkOrderUpdateObj;
import com.crowninteractive.smsportal.websrv.workforce.WorkOrderUpdateObjResponse;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Oluremi Adekanmbi <oluremi.adekanmbi@etranzact.com>
 */
public class UtilMethods {

    private static int sequence;
    private static final Logger L = Logger.getLogger(UtilMethods.class);
    private static final Gson GSON = new Gson();
    private static final EMCC EMCC = new EMCC();
    private static final Config CONFIG = Config.getInstance();

    private static final String WSDL1 = CONFIG.getESBGeneralURL();
    private static final String WSDL2 = CONFIG.getESBWorkForceURL();
    private static final String WSDL3 = CONFIG.getESBWalletURL();
    private static final String WSDL4 = CONFIG.getESBAccountURL();
    private static final String WSDL5 = CONFIG.getESBSMSModuleURL();
    private static final String WSDL6 = CONFIG.getUCGVoucherURL();
    private final SMSProcessor sp = new SMSProcessor();
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

    public static String getShortCodeStyle(String shortCode) {
        String[] split = shortCode.split("\\*");
        StringBuilder sb = new StringBuilder("*");
        for (int i = 0; i < split.length; i++) {
            String string = split[i];
            switch (i) {
                case 0:
                    break;
                case 1:
                case 2:
                    sb.append(string).append("*");
                    break;
                default:
                    int passedVal;
                    try {
                        passedVal = Integer.parseInt(string);
                        if (passedVal > 49) {
                            sb.append("ANY").append("*");
                        } else {
                            sb.append(string).append("*");
                        }
                    } catch (NumberFormatException numberFormatException) {
                        sb.append("ANY").append("*");
                    }
                    break;
            }
        }
        String finalString = sb.toString().substring(0, sb.toString().length() - 1);
        return finalString + "#";
    }

    public static String getShortCodeStyle2(String shortCode) {
        String substring = shortCode.substring(0, shortCode.length() - 1);
        String[] split = substring.split("\\*");
        String exempted = "5433";
        StringBuilder sb = new StringBuilder("*");
        for (int i = 0; i < split.length; i++) {
            String string = split[i];

            switch (i) {
                case 0:
                    break;
                case 1:
                    sb.append(string).append("*");
                    break;
                default:
                    int passedVal;
                    try {
                        passedVal = Integer.parseInt(string);
                        if (exempted.contains(string)) {
                            sb.append(string).append("*");
                        } else if (passedVal > 50) {
                            sb.append("ANY").append("*");
                        } else {
                            sb.append(string).append("*");
                        }
                    } catch (NumberFormatException numberFormatException) {
                        sb.append("ANY").append("*");
                    }
                    break;
            }
        }
        String finalString = sb.toString().substring(0, sb.toString().length() - 1);
        return finalString + "#";
    }

    public static synchronized String generateReference(String mobile) {
        String[] alphabets = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        String uniqueId = "02JT" + alphabets[new Random(System.currentTimeMillis()).nextInt(26)] + alphabets[new Random(System.nanoTime()).nextInt(26)] + mobile.substring(5, 6);

        int pos = new Random(System.currentTimeMillis()).nextInt(mobile.length());
        uniqueId += mobile.substring(pos, pos + 1);
        java.text.DecimalFormat sequ = new java.text.DecimalFormat("000");
        long nxtSeq = sequence++;
        if (nxtSeq > 998) {
            sequence = 0;
        }
        String inCnt = sequ.format(nxtSeq);
        uniqueId += inCnt;
        String hash = md5(mobile + new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date()));
        pos = new Random(System.nanoTime()).nextInt(32);
        uniqueId += hash.substring(pos, pos + 1).toUpperCase();
        pos = new Random(System.currentTimeMillis()).nextInt(32);
        uniqueId += hash.substring(pos, pos + 1).toUpperCase();
        String randomNo = new Random(System.nanoTime()).nextInt(10000) + "";
        while (randomNo.length() < 4) {
            randomNo = "0" + randomNo;
        }
        uniqueId += randomNo;
        return uniqueId;
    }

    public static String md5(String value) {
        String macValue = "";

        try {
            MessageDigest mdEnc = MessageDigest.getInstance("MD5"); // Encryption
            // algorithm
            mdEnc.update(value.getBytes(), 0, value.length());
            macValue = new BigInteger(1, mdEnc.digest()).toString(16);
            int len = 32 - macValue.length();
            for (int i = 0; i < len; i++) {
                macValue = "0" + macValue;
            }
        } catch (Exception e) {
            L.info("Error generating Check Value :: "
                    + e.getMessage());
            macValue = "";
        }

        return macValue;
    }

    public static String sendGet(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        L.info("\nSending 'GET' request to URL : " + url);
        L.info("Response Code : " + responseCode);
        StringBuilder response = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            L.info("RESPONSE FROM SERVICE:: " + response.toString());
        } catch (Exception ex) {

        }
        return response.toString();
    }

    private static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public static String getAmount(String initialCode) {
        String[] split = initialCode.split("\\*");
        return split[3].replace("#", "");
    }

    public static String getWFMStatusForUSSD(String msisdn, String ticketId) {
        try {
            String returnMessage = null;
            StringBuilder sb = new StringBuilder(CONFIG.getWFMStatusList());
            sb.append("/").append(ticketId);
            sb.append("/").append("0").append(msisdn(msisdn));
            L.info(sb.toString());
            String message = HttpUtil.sendGet(sb.toString());
            WFMResponse wresp = GSON.fromJson(message, WFMResponse.class);
            List<WFM> object = wresp.getObject();
            if (!object.isEmpty()) {
                sb = new StringBuilder();
                int counter = 1;
                for (WFM w : object) {
                    sb.append(counter).append("=").append(w.getName()).append("\n");
                    counter++;
                }
                returnMessage = sb.toString();
            }
            returnMessage = returnMessage.substring(0, 120);
            return returnMessage;
        } catch (Exception exception) {
            L.info("Exception thrown calling WFM >>>> " + exception.getLocalizedMessage());
        }
        return null;
    }

    public static List<WFM> getWFMStatus(String msisdn, String ticketId) {
        try {
            StringBuilder sb = new StringBuilder(CONFIG.getWFMStatusList());
            sb.append("/").append(ticketId);
            sb.append("/").append("0").append(msisdn(msisdn));
            L.info(sb.toString());
            String message = HttpUtil.sendGet(sb.toString());
            WFMResponse wresp = GSON.fromJson(message, WFMResponse.class);
            List<WFM> wfms = wresp.getObject();
            return wfms;
        } catch (Exception exception) {
            L.info("Exception thrown calling WFM >>>> " + exception.getLocalizedMessage());
        }
        return null;
    }

    public static String updateWFMStatus(String msisdn, WFM2 w) {
        String returnMessage = "";
        try {
            StaffDetail validatePhone = validatePhone(msisdn);
            if (validatePhone != null) {
                StringBuilder sb = new StringBuilder(CONFIG.getWFMUpdateStatus());
                sb.append("?phone=").append("0").append(msisdn(msisdn));
                L.info(sb.toString());
                String sendPost = HttpUtil.sendPost(sb.toString(), GSON.toJson(w));
                L.info(sendPost);
                WFMResponse2 wfmResp = GSON.fromJson(sendPost, WFMResponse2.class);
                List<WFM3> obj = wfmResp.getObject();
                if (obj.isEmpty()) {
                    if (wfmResp.getDesc().startsWith("System")) {
                        returnMessage = "Please send Track." + w.getTicketId() + " to confirm the status of this ticket id. Thank you. Powered by EKEDP";
                    } else if (wfmResp.getDesc().startsWith("Invalid")) {
                        returnMessage = "This phone number is attached to more than one User. Please contact your system administrator. Thank you.";
                    } else if (wfmResp.getDesc().startsWith("Successful")) {
                        returnMessage = "Ticket #" + w.getTicketId() + " has been updated to " + w.getStatusName().trim() + " status. Powered by EKEDP.";
                    } else {
                        returnMessage = wfmResp.getDesc() + ". Powered by EKEDP.";
                    }
                } else {
                    WFM3 w3 = obj.get(0);
                    sb = new StringBuilder();
                    sb.append("Payment Order Id: ").append(w3.getOrderId()).append(" for ").append(w3.getChargeName()).append(" has been generated for ").append(w3.getBillingAccount());
                    sb.append(" at ").append(w3.getAddress().replaceAll("\\+", " ")).append(" and the charge is N").append(w3.getDebtOnAccount());
                    returnMessage = sb.toString();
                }
                return returnMessage;
            }
        } catch (Exception exception) {
            L.info("Exception thrown calling WFM >>>> " + exception.getLocalizedMessage());
        }
        return null;
    }

    public static String msisdn(String msisdn) {
        if (msisdn.startsWith("234")) {
            return msisdn.substring(3);
        }
        return msisdn;
    }

    public static String msisdnToInternational(String msisdn) {
        if (msisdn.startsWith("234")) {
            return msisdn;
        } else if (msisdn.startsWith("0")) {
            msisdn = msisdn.substring(0, msisdn.length() - 1);
            return "234" + msisdn;
        } else {
            return "234" + msisdn;
        }
    }

    public static String submitMeterReading(String msisdn, String meterNumber, String meterReading) {
        try {
            StaffDetail validatePhone = validatePhone(msisdn);
            if (validatePhone != null) {
                String sb2 = CONFIG.getSubmitMeterReadingURL();
                EMCC.setMeterNumber(meterNumber);
                EMCC.setMeterReading(meterReading);
                EMCC.setDistrict(validatePhone.getDistricts());
                EMCC.setPhone(msisdn(msisdn));
                EMCC.setName(validatePhone.getName());
                L.info(sb2);
                String retVal = HttpUtil.sendPost(sb2, GSON.toJson(EMCC));
                L.info(retVal);
                EMCCResponse eResp = GSON.fromJson(retVal, EMCCResponse.class);
                return eResp.getDesc() + ". Powered by EKEDP";
            } else {
                return "Invalid Staff Phone number. Powered by EKEDP";
            }

        } catch (Exception exception) {
            L.info("Exception thrown calling WFM >>>> " + exception.getLocalizedMessage());
        }
        return null;
    }

    public static StaffDetail validatePhone(String msisdn) {
        try {
            //StringBuilder sb2 = new StringBuilder("http://81.26.64.42:8084/users/validatebyphone/");
            StringBuilder sb = new StringBuilder(CONFIG.getWFMValidateURL());
            sb.append("0").append(msisdn(msisdn));
            L.info(sb.toString());
            String retVal;
            retVal = HttpUtil.sendGet(sb.toString());
            L.info(retVal);
            StaffValidate sv = new Gson().fromJson(retVal, StaffValidate.class);
            L.info(sv.toString());
            if (sv.isSuccess()) {
                return sv.getData()[0];
            }
        } catch (Exception exception) {
            return null;
        }
        return null;
    }

    public static String trackTicketId(String ticketId) {
        String returnMessage;
        WorkOrderUpdateObjResponse wrr = esbWorkforce.getWorkOrderUpdates("SL_SMS", "01", ticketId);
        if (wrr == null) {
            returnMessage = "We cannot fetch the current status of this ticket ID at the moment. Please try again later. Powered by EKEDP.";
        } else if (wrr.getRetn() == 0) {
            if (wrr.getSimpleObjList().isEmpty()) {
                returnMessage = "There are currently no update for this ticket id. Powered by EKEDP.";
            } else {
                int size = wrr.getSimpleObjList().size();
                List<WorkOrderUpdateObj> simpleObjList = wrr.getSimpleObjList();
                if (simpleObjList.isEmpty()) {
                    returnMessage = "There are currently no update for this ticket id. Powered by EKEDP.";
                } else {
                    WorkOrderUpdateObj remark = simpleObjList.get(size - 1);
                    StringBuilder sb = new StringBuilder("Ticket Id : ");
                    sb.append(ticketId).append(" status is ");
                    sb.append(remark.getStatus());
                    sb.append(" with the comment : ");
                    if (remark.getComment() == null) {
                        sb.append("Open request");
                    } else {
                        sb.append(remark.getComment());
                    }
                    sb.append(". Powered by EKEDP.");
                    returnMessage = sb.toString();
                }
            }
        } else {
            returnMessage = "The ticket id provided does not exist. Powered by EKEDP.";
        }
        return returnMessage;
    }

    public static String getLastPayment(String accountNumber) {
        String returnMessage;
        try {
            String sendGet = HttpUtil.sendGet(CONFIG.getUCGLastPaymet() + accountNumber);
            UCG ucg = new Gson().fromJson(sendGet, UCG.class);
            System.out.println(ucg);
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
        } catch (Exception exception) {
            returnMessage = "We cannot process this request at the moment. Please try again later. Powered by EKEDP.";
        }
        return returnMessage;
    }

    public static String getPowerAvailability(String accountNumber) {
        StringBuilder sb = new StringBuilder(CONFIG.getEMCCFeederStatus());
        sb.append(accountNumber);
        FeederStatusResponse fs = null;
        try {
            String retVal2 = HttpUtil.sendGet(sb.toString());
            fs = new Gson().fromJson(retVal2, FeederStatusResponse.class);
            sb = new StringBuilder();
            sb.append("Dear ").append(accountNumber).append(" your feeder ").append(fs.getObject().getLowVoltageFeederId().getName().trim());
            //sb.append(" coming from ").append(fs.getObject().getInjectionSubstationId().getName().trim()).append(" Injection Substation");
            sb.append(" is currently ").append(fs.getObject().getStatus()).append(". Last updated on ").append(fs.getObject().getUpdated());
            if (fs.getObject() != null) {
                FeederStatus f = fs.getObject();
                if (f != null) {
                    if (f.getLowVoltageFeederId() != null) {
                        retVal2 = HttpUtil.sendGet(CONFIG.getEMCCCurrentSchedule() + f.getLowVoltageFeederId().getToken());
                        PowerScheduleResponse pss = new Gson().fromJson(retVal2, PowerScheduleResponse.class);
                        if (pss != null) {
                            if (pss.getResp() == 0) {
                                if (fs.getObject().getStatus().equals("OFF")) {
                                    sb.append(". Power should be restored on or before ").append(pss.getObject().getStartTime());
                                }
                            }
                        }
                    }
                }
            }
            sb.append(". Powered by EKEDP.");
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
