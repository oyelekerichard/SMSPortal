
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.parser;

import com.crowninteractive.smsportal.dto.BaseResponse;

import com.crowninteractive.smsportal.dto.EMCC;
import com.crowninteractive.smsportal.dto.EMCCResponse;
import com.crowninteractive.smsportal.dto.FeederStatus;
import com.crowninteractive.smsportal.dto.FeederStatusResponse;
import com.crowninteractive.smsportal.dto.PowerScheduleResponse;
import com.crowninteractive.smsportal.dto.Response;
import com.crowninteractive.smsportal.dto.StaffDetail;
import com.crowninteractive.smsportal.dto.StaffValidate;
import com.crowninteractive.smsportal.dto.StaffValidateError;
import com.crowninteractive.smsportal.dto.UCG;
import com.crowninteractive.smsportal.dto.WFM;
import com.crowninteractive.smsportal.dto.WFM2;
import com.crowninteractive.smsportal.dto.WFM3;
import com.crowninteractive.smsportal.dto.WFMResponse;
import com.crowninteractive.smsportal.dto.WFMResponse2;
import com.crowninteractive.smsportal.enums.MeterType;
import com.crowninteractive.smsportal.model.Registrations;
import com.crowninteractive.smsportal.model.SIncoming;
import com.crowninteractive.smsportal.model.SOutgoing;
import com.crowninteractive.smsportal.service.DBAccessBean;
import com.crowninteractive.smsportal.service.SMSServiceImpl;
import com.crowninteractive.smsportal.sms.SMS;
import com.crowninteractive.smsportal.util.Config;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import com.crowninteractive.smsportal.util.HttpUtil;
import com.crowninteractive.smsportal.util.ResponseCodes;
import com.crowninteractive.smsportal.util.UtilMethods;
import com.crowninteractive.smsportal.util.Validate;
import com.crowninteractive.smsportal.util.VoucherUtil;
import com.crowninteractive.smsportal.websrv.EsbAccount;
import com.crowninteractive.smsportal.websrv.general.EsbGeneral;
import com.crowninteractive.smsportal.websrv.sms.ReservedInventory;
import com.crowninteractive.smsportal.websrv.sms.SmsModule;
import com.crowninteractive.smsportal.websrv.sms.SmsPickInventoryResponse;
import com.crowninteractive.smsportal.websrv.sms.SmsReservedInventoryResponse;
import com.crowninteractive.smsportal.websrv.wallet.EsbWallet;
import com.crowninteractive.smsportal.websrv.workforce.EsbWorkforce;
import com.crowninteractive.smsportal.websrv.workforce.WorkOrderUpdateObj;
import com.crowninteractive.smsportal.websrv.workforce.WorkOrderUpdateObjResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author Adekanmbi Oluremi
 * <adekanmbi.oluremi@ucitech.com.ng>
 */
public class EkoDistribution {

    private static final Logger LOG = Logger.getLogger(EkoDistribution.class);
    //private final EkoAcronyms ekoAcronyms = EkoAcronyms.getInstance();
    private final DBAccessBean accessbean;
    private static EkoDistribution INSTANCE = new EkoDistribution();
    private static final Config CONFIG = Config.getInstance();

    private EkoDistribution() {
        accessbean = new DBAccessBean("SMSPortalPU");
    }

    public static EkoDistribution getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EkoDistribution();
        }
        return INSTANCE;
    }

    public String dispatchByKeyword(SMS sms,
            EsbGeneral smsModule,
            EsbWorkforce workforce,
            EsbWallet esbWallet,
            EsbAccount esbAccount, SmsModule smsmod, String uniqueTransId) {
        String returnMessage = "", returnVal = "";
        Gson gson = new Gson();
        EMCC emcc = new EMCC();
        EMCCResponse eResp;
        StaffDetail sd = null;
        StringBuilder sbuilder;
        String keyword;
        if (sms.getText().contains(".")) {
            keyword = sms.getText().split("\\.")[0];
        } else {
            keyword = sms.getText().split(" ")[0];
        }
        LOG.info(keyword);
        accessbean.create(new SIncoming(sms, uniqueTransId));
        switch (keyword.toUpperCase().trim()) {
            case "BILL":
                //<editor-fold defaultstate="collapsed" desc="BILL">
                LOG.info("Checking billing");
                //The latest bill amount for {account number} is {amount} and
                //the consumption is {energy consumption} as of {bill due date and time}. Powered by EKEDP.
                String secondPart = sms.getText().split("\\.")[1];
                com.crowninteractive.smsportal.websrv.general.InvoiceResponse resp = smsModule.getLastUtilityAccountInvoice("SL_SMS", secondPart);
                if (resp == null) {
                    returnMessage = "We cannot fetch your bill at the moment. Please try again later. Powered by EKEDP.";
                } else if (resp.getRetn() == 0) {
                    if (resp.getInvoices().isEmpty()) {
                        returnMessage = "You do not have any current bill for this period. Powered by EKEDP.";
                    } else {
                        com.crowninteractive.smsportal.websrv.general.Billing bill = resp.getInvoices().get(0);
                        StringBuilder sb = new StringBuilder();
                        sb.append("The last amount for ");
                        sb.append(secondPart);
                        sb.append(" is NGN ");
                        sb.append(bill.getTotalCharge());
                        sb.append(" and the consumption is ");
                        sb.append(bill.getConsumption());
                        sb.append(" as of ");
                        sb.append(bill.getBillDueDate());
                        sb.append(". Powered by EKEDP.");
                        returnMessage = sb.toString();
                    }
                } else {
                    returnMessage = ResponseCodes.getDefaultMessageFor(resp.getRetn());
                }
                //</editor-fold>
                break;
            case "FULLBILL":
                //<editor-fold defaultstate="collapsed" desc="FULL BILL">
                LOG.info("Checking full billing");
                String fb = sms.getText().split("\\.")[1];
                com.crowninteractive.smsportal.websrv.general.InvoiceResponse fbResp = smsModule.getLastUtilityAccountInvoice("SL_SMS", fb);
                if (fbResp == null) {
                    returnMessage = "We cannot fetch your bill at the moment. Please try again later. Powered by EKEDP.";
                } else if (fbResp.getRetn() == 0) {
                    //This is the latest bill for {Account Number}, {Latest Bill produced for account}
                    if (fbResp.getInvoices().isEmpty()) {
                        returnMessage = "You do not have any current bill for this period. Powered by EKEDP.";
                    } else {
                        //Your bill is {net arrears}+{VAT}+{Energy charge}={closing bill} as of {Bill due date and time}. Powered by EKEDP
                        com.crowninteractive.smsportal.websrv.general.Billing bill2 = fbResp.getInvoices().get(0);
                        StringBuilder sb = new StringBuilder();
                        sb.append("Your bill is ");
                        sb.append(bill2.getNetArrears());
                        sb.append(" + ");
                        sb.append(bill2.getVat());
                        sb.append(" + ");
                        sb.append(bill2.getEnergyCharge());
                        sb.append(" = ");
                        sb.append((bill2.getNetArrears() + bill2.getVat() + bill2.getEnergyCharge()));
                        sb.append(" as of ");
                        sb.append(DateTimeUtil.getCurrentDate());
                        sb.append(". Powered by EKEDP.");
                        returnMessage = sb.toString();
                    }
                } else {
                    returnMessage = ResponseCodes.getDefaultMessageFor(fbResp.getRetn());
                }
                //</editor-fold>
                break;
            case "PAYMENT":
                //<editor-fold defaultstate="collapsed" desc="PAYMENT">
                LOG.info("Checking last payment");
                String lp = sms.getText().split("\\.")[1];
                try {
                    String sendGet = HttpUtil.sendGet(CONFIG.getUCGLastPaymet() + lp);
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

                    sd = UtilMethods.validatePhone(sms.getMsisdn());

                } catch (Exception exception) {
                    returnMessage = "We cannot process this request at the moment. Please try again later. Powered by EKEDP.";
                }
                //</editor-fold>
                break;
            case "ACCOUNT":
                //<editor-fold defaultstate="collapsed" desc="ACCOUNT">
                LOG.info("Checking net arrears with ACCOUNT keyword");
                String acc = sms.getText().split("\\.")[1];
                com.crowninteractive.smsportal.websrv.general.InvoiceResponse accResp = smsModule.getLastUtilityAccountInvoice("SL_SMS", acc);
                if (accResp == null) {
                    returnMessage = "We cannot fetch your bill at the moment. Please try again later. Powered by EKEDP.";
                } else if (accResp.getRetn() == 0) {
                    //The provided account is in arrears of N150,349.45
                    if (accResp.getInvoices().isEmpty()) {
                        returnMessage = "You do not have any current bill for this period. Powered by EKEDP.";
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("The net arrears for ekedp account ");
                        sb.append(acc);
                        sb.append(" is NGN");
                        Double value = 0.00;
                        if (accResp.getInvoices().get(0).getPreviousBalance() == null) {
                            sb.append(value);
                        } else {
                            sb.append(accResp.getInvoices().get(0).getPreviousBalance());
                        }
                        sb.append(" and the last payment was recieved on ");
                        sb.append(accResp.getInvoices().get(0).getLastDate());
                        sb.append(". Powered by EKEDP.");
                        returnMessage = sb.toString();
                    }
                } else {
                    returnMessage = ResponseCodes.getDefaultMessageFor(accResp.getRetn());
                }
                //</editor-fold>
                break;
            case "USAGE":
                //<editor-fold defaultstate="collapsed" desc="USAGE">
                LOG.info("Checking USAGE keyword");
                //Your consumption as of {bill due date and time} on {account number} is {Energy consumption). Powered by EKEDP
                String usage = sms.getText().split("\\.")[1];
                com.crowninteractive.smsportal.websrv.general.InvoiceResponse usageResp = smsModule.getLastUtilityAccountInvoice("SL_SMS", usage);
                if (usageResp == null) {
                    returnMessage = "We cannot fetch your bill at the moment. Please try again later. Powered by EKEDP.";
                } else if (usageResp.getRetn() == 0) {
                    //The provided account is in arrears of N150,349.45
                    if (usageResp.getInvoices().isEmpty()) {
                        returnMessage = "You do not have any current bill for this period. Powered by EKEDP.";
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Your consumption as of ");
                        sb.append(DateTimeUtil.getCurrentDate());
                        sb.append(" on ");
                        sb.append(usage);
                        sb.append(" is ");
                        sb.append(usageResp.getInvoices().get(0).getConsumption());
                        sb.append("Kwh. Powered by EKEDP.");
                        returnMessage = sb.toString();
                    }
                } else {
                    returnMessage = ResponseCodes.getDefaultMessageFor(usageResp.getRetn());
                }
                //</editor-fold>
                break;
            case "TRACK":
                //<editor-fold defaultstate="collapsed" desc="TRACK">
                LOG.info("Checking track");
                String ticketId = sms.getText().split("\\.")[1];
                WorkOrderUpdateObjResponse wrr = workforce.getWorkOrderUpdates("SL_SMS", "01", ticketId);
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
                //</editor-fold>
                break;
            case "REG":
            case "REGISTER":
                //<editor-fold defaultstate="collapsed" desc="REGISTER">
                LOG.info("Checking REGISTER or REG");
                String register = sms.getText().split("\\.")[1];
                String accountNumberOrMeterNumber = sms.getText().split("\\.")[2];
                if (accountNumberOrMeterNumber.contains("/")) {
                    accountNumberOrMeterNumber = accountNumberOrMeterNumber.replaceAll("/", "");
                }
                switch (register.toUpperCase().trim()) {
                    case "LOAD":
                    case "ACCOUNT":
                        Registrations smsr = new Registrations(sms.getMsisdn(), accountNumberOrMeterNumber, MeterType.NORMAL);
                        Registrations r = SMSServiceImpl.getInstance().createReg(smsr);
                        LOG.info("" + r);
                        sbuilder = new StringBuilder(CONFIG.getEMCCUpdatePhoneNumber());
                        emcc.setAccountNumber(accountNumberOrMeterNumber);
                        emcc.setPhone("0" + sms.getMsisdn());
                        LOG.info(sbuilder.toString());
                        try {
                            returnVal = HttpUtil.sendPost(sbuilder.toString(), gson.toJson(emcc));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        LOG.info(returnVal);
                        eResp = new Gson().fromJson(returnVal, EMCCResponse.class);
                        if (eResp.getResp() == 0) {
                            returnMessage = "You have successfully registered your account number " + smsr.getAccountORMeterNumber() + " for " + sms.getMsisdn() + ". Powered by EKEDP.";
                        } else {
                            returnMessage = eResp.getDesc() + ". Powered by EKEDP.";
                        }
                        break;
                    case "TOKEN":
                    case "METERNUMBER":
                    case "METER":
                        Registrations s = new Registrations(sms.getMsisdn(), accountNumberOrMeterNumber, MeterType.PPM);
                        Registrations reg = SMSServiceImpl.getInstance().createReg(s);
                        LOG.info("" + reg);
                        sbuilder = new StringBuilder(CONFIG.getEMCCUpdatePhoneNumber());
                        emcc.setMeterNumber(accountNumberOrMeterNumber);
                        emcc.setPhone("0" + sms.getMsisdn());
                        LOG.info(sbuilder.toString());
                        try {
                            returnVal = HttpUtil.sendPost(sbuilder.toString(), gson.toJson(emcc));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        LOG.info(returnVal);
                        eResp = new Gson().fromJson(returnVal, EMCCResponse.class);
                        if (eResp.getResp() == 0) {
                            returnMessage = "You have successfully registered your meter number " + s.getAccountORMeterNumber() + " for " + sms.getMsisdn() + ". Powered by EKEDP.";
                        } else {
                            returnMessage = eResp.getDesc() + ". Powered by EKEDP.";
                        }
                        break;
                    default:
                        returnMessage = "Unknown registration command. Powered by EKEDP.";
                        break;
                }
                //</editor-fold>
                break;
            case "UNREG":
            case "UNREGISTER":
                //<editor-fold defaultstate="collapsed" desc="UNREGISTER">
                LOG.info("Checking REGISTER or REG");
                String register2 = sms.getText().split("\\.")[1];
                String accountNumberOrMeterNumber2 = sms.getText().split("\\.")[2];
                if (accountNumberOrMeterNumber2.contains("/")) {
                    accountNumberOrMeterNumber = accountNumberOrMeterNumber2.replaceAll("/", "");
                }
                switch (register2.toUpperCase().trim()) {
                    case "LOAD":
                    case "ACCOUNT":
                        Registrations smsr = new Registrations(sms.getMsisdn(), accountNumberOrMeterNumber2, MeterType.NORMAL);
                        Registrations r = SMSServiceImpl.getInstance().createReg(smsr);
                        LOG.info("" + r);
                        sbuilder = new StringBuilder(CONFIG.getEMCCRemovePhoneNumber());
                        emcc.setAccountNumber(accountNumberOrMeterNumber2);
                        emcc.setPhone("0" + sms.getMsisdn());
                        LOG.info(sbuilder.toString());
                        try {
                            returnVal = HttpUtil.sendPost(sbuilder.toString(), gson.toJson(emcc));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        LOG.info(returnVal);
                        eResp = new Gson().fromJson(returnVal, EMCCResponse.class);
                        if (eResp.getResp() == 0) {
                            returnMessage = "You have successfully unregistered your account number " + smsr.getAccountORMeterNumber() + " for " + sms.getMsisdn() + ". Powered by EKEDP.";
                        } else {
                            returnMessage = eResp.getDesc() + ". Powered by EKEDP.";
                        }
                        break;
                    case "TOKEN":
                    case "METERNUMBER":
                    case "METER":
                        Registrations s = new Registrations(sms.getMsisdn(), accountNumberOrMeterNumber2, MeterType.PPM);
                        Registrations reg = SMSServiceImpl.getInstance().createReg(s);
                        LOG.info("" + reg);
                        sbuilder = new StringBuilder(CONFIG.getEMCCRemovePhoneNumber());
                        emcc.setMeterNumber(accountNumberOrMeterNumber2);
                        emcc.setPhone("0" + sms.getMsisdn());
                        LOG.info(sbuilder.toString());
                        try {
                            returnVal = HttpUtil.sendPost(sbuilder.toString(), gson.toJson(emcc));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        LOG.info(returnVal);
                        eResp = new Gson().fromJson(returnVal, EMCCResponse.class);
                        if (eResp.getResp() == 0) {
                            returnMessage = "You have successfully unregistered your meter number " + s.getAccountORMeterNumber() + " for " + sms.getMsisdn() + ". Powered by EKEDP.";
                        } else {
                            returnMessage = eResp.getDesc() + ". Powered by EKEDP.";
                        }
                        break;
                    default:
                        returnMessage = "Unknown registration command. Powered by EKEDP.";
                        break;
                }
                //</editor-fold>
                break;
            case "RECONNECT":
            case "PAYORDERID":
                //<editor-fold defaultstate="collapsed" desc="RECONNECT">
                LOG.info("Checking reconnect");
                String orderId = sms.getText().split("\\.")[1];
                String pinNos = sms.getText().split("\\.")[2];
                BaseResponse r = VoucherUtil.processDetails(sms.getMsisdn(), orderId, pinNos, "R");
                switch (r.getRetn()) {
                    case 0:
                        returnMessage = "Your payment towards order id " + orderId + " was successful. Powered by EKEDP.";
                        break;
                    default:
                        returnMessage = "Your payment was not successful. Please keep the voucher pin safe and try again later. Powered by EKEDP.";
                        break;
                }
                //</editor-fold>
                break;
            case "LOAD":
            case "PAYBILL":
                //<editor-fold defaultstate="collapsed" desc="LOAD">
                LOG.info("Checking Load");
                //Load.Pin.AccountNo
                try {
                    String accountNos = sms.getText().split("\\.")[1];
                    String loadPin = sms.getText().split("\\.")[2];
                    BaseResponse response = VoucherUtil.processDetails(sms.getMsisdn(), accountNos, loadPin, "A");
                    switch (response.getRetn()) {
                        case 0:
                            returnMessage = "Your voucher is successfully loaded and it will be credited towards your bill. TxRef: " + (String) response.getObj() + " Powered by EKEDP.";
                            break;
                        default:
                            returnMessage = "Your payment was not successful. Please keep the voucher pin safe and try again later. Powered by EKEDP.";
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    LOG.info("ArrayIndexOutOfBounds");
                    LOG.info(sms.getMsisdn());
                    String loadPin = sms.getText().split("\\.")[1];
                    //<editor-fold defaultstate="collapsed" desc="FINDING ACCOUNTS">
                    DBAccessBean accessbean = new DBAccessBean("SL_SMSPU");
                    String query = "SELECT s FROM Registrations s WHERE s.phone =:phone AND s.meterType =:meterType";
                    TypedQuery<Registrations> tq = accessbean.createQuery(query, Registrations.class);
                    tq.setParameter("phone", sms.getMsisdn());
                    tq.setParameter("meterType", MeterType.NORMAL);
                    List<Registrations> resultList = tq.getResultList();
                    //</editor-fold>

                    if (!resultList.isEmpty()) {
                        LOG.info("Registered accounts for this number are as follows");
                        for (Registrations registrations : resultList) {
                            LOG.info(registrations.toString());
                        }
                        LOG.info("Using last registered account");
                        Registrations reg = resultList.get(resultList.size() - 1);
                        LOG.info(reg.toString());
                        Response process = Validate.process(reg.getAccountORMeterNumber(), true);
                        if (process.isEntity()) {
                            BaseResponse response = VoucherUtil.processDetails(sms.getMsisdn(), reg.getAccountORMeterNumber(), loadPin, "A");
                            switch (response.getRetn()) {
                                case 0:
                                    returnMessage = "Your voucher is successfully loaded and it will be credited towards your bill. TxRef: " + (String) response.getObj() + " Powered by EKEDP.";
                                    break;
                                default:
                                    LOG.info(response.getDesc());
                                    returnMessage = response.getDesc() + ". Powered by EKEDP.";
                                    break;
                            }
                        } else {
                            returnMessage = "Your meter number is either incorrect or could not be verified at the moment. Powered by EKEDP.";
                        }

                    } else {
                        LOG.info("Reg is null!!!");
                        returnMessage = "You do not have any account registered to this number. Powered by EKEDP";
                    }
                }

                //</editor-fold>
                break;
            case "TOKEN":
            case "BUYTOKEN":
                //<editor-fold defaultstate="collapsed" desc="TOKEN">
                LOG.info("Checking TOKEN");
                //token.Pin.MeterNo
                try {
                    String meterNos = sms.getText().split("\\.")[1];
                    String pin = sms.getText().split("\\.")[2];
                    Response process = Validate.process(meterNos, true);
                    System.out.println("Response Obj ------------>  " + process);

                    if (process.isEntity()) {
                        BaseResponse res = VoucherUtil.processDetails(sms.getMsisdn(), meterNos, pin, "M");
                        switch (res.getRetn()) {
                            case 0:
                                returnMessage = "Your voucher was successfully loaded. Your token will be sent shortly. TxRef : " + (String) res.getObj() + " Powered by EKEDP.";
                                break;
                            default:
                                returnMessage = "Your payment was not successful. Please keep the voucher pin safe and try again later. Powered by EKEDP.";
                                break;
                        }
                    } else {
                        returnMessage = "Your meter number is either incorrect or could not be verified at the moment. Powered by EKEDP.";
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
//               if (process.isEntity()) {
                    //<editor-fold defaultstate="collapsed" desc="FINDING ACCOUNTS">
                    DBAccessBean accessbean = new DBAccessBean("SL_SMSPU");
                    String query = "SELECT s FROM Registrations s WHERE s.phone =:phone AND s.meterType =:meterType";
                    TypedQuery<Registrations> tq = accessbean.createQuery(query, Registrations.class);
                    tq.setParameter("phone", sms.getMsisdn());
                    tq.setParameter("meterType", MeterType.PPM);
                    List<Registrations> resultList = tq.getResultList();
                    //</editor-fold>

                    if (!resultList.isEmpty()) {
                        LOG.info("Registered accounts for this number are as follows");
                        for (Registrations registrations : resultList) {
                            LOG.info(registrations.toString());
                        }
                        LOG.info("Using last registered account");
                        Registrations reg = resultList.get(resultList.size() - 1);
                        LOG.info(reg.toString());
                        Response process = Validate.process(reg.getAccountORMeterNumber(), true);
                        if (process.isEntity()) {
                            pinNos = sms.getText().split("\\.")[1];
                            BaseResponse rw = VoucherUtil.processDetails(sms.getMsisdn(), reg.getAccountORMeterNumber(), pinNos, "M");
                            switch (rw.getRetn()) {
                                case 0:
                                    returnMessage = "Your voucher was successfully loaded. Your token will be sent shortly. TxRef : " + (String) rw.getObj() + " Powered by EKEDP.";
                                    break;
                                default:
                                    LOG.info(rw.getDesc());
                                    returnMessage = rw.getDesc() + ". Powered by EKEDP.";
                                    break;
                            }
                        } else {
                            returnMessage = "You do not have any account registered to this number. Powered by EKEDP";
                        }
                    } else {
                        returnMessage = "Your meter number is either incorrect or could not be verified at the moment. Powered by EKEDP.";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    returnMessage = "Your meter number is either incorrect or could not be verified at the moment. Powered by EKEDP.";
                }

                //</editor-fold>
                break;
            case "GETINVENTORY":
                //<editor-fold defaultstate="collapsed" desc="GETINVENTORY">
                String inventoryTicketId = sms.getText().split("\\.")[1];
                SmsReservedInventoryResponse reservedInventory
                        = smsmod.getReservedInventory(inventoryTicketId, "SL_SMS");
                Integer retn = reservedInventory.getRetn();
                switch (retn) {
                    case 0:
                        StringBuilder sbi = new StringBuilder(inventoryTicketId);
                        sbi.append(", ").append("Approver: ").append(reservedInventory.getApprover());
                        for (ReservedInventory ri : reservedInventory.getReservedInventories()) {
                            sbi.append(ri.getListNumber())
                                    .append(" ").append(ri.getInventoryName())
                                    .append(" ").append(ri.getQuantity())
                                    .append("-").append(ri.getStore());
                        }
                        returnMessage = sbi.toString();
                        break;
                    default:
                        returnMessage = "You have either entered an invalid ticketId or this order is still being processed. Powered by EKEDC.";
                        break;
                }
                //</editor-fold>
                break;
            case "ISSUE":
                //<editor-fold defaultstate="collapsed" desc="ISSUE">
                String[] split = sms.getText().split("\\.");
                if (split[3].toUpperCase().endsWith("ALL")) { //Issue.ticketId.listnumber.all
                    SmsPickInventoryResponse pickInventory = smsmod.pickInventory(split[3], split[1], "SL_SMS");
                    StringBuilder s = new StringBuilder("Issuance successful for ");
                    s.append(split[1]).append(" ALL ").append("of ").append(pickInventory.getInventories().get(0).getInventoryName());
                    s.append("Approved by ").append(pickInventory.getFinalApproval()).append(" for ").append(pickInventory.getEngineerName());
                    s.append(". ").append(pickInventory.getDateTime());
                    returnMessage = s.toString();
                } else {//Issue.ticketId.listnumber.quantity
                    SmsPickInventoryResponse pickInventory = smsmod.pickInventory(split[3], split[1], "SL_SMS");
                    StringBuilder s = new StringBuilder("Issuance successful for ");
                    s.append(split[1]).append(split[3]).append("of ").append(pickInventory.getInventories().get(0).getInventoryName());
                    s.append("Approved by ").append(pickInventory.getFinalApproval()).append(" for ").append(pickInventory.getEngineerName());
                    s.append(". ").append(pickInventory.getDateTime());
                    returnMessage = s.toString();
                }
                //</editor-fold>
                break;
            case "STAFF":
                //<editor-fold defaultstate="collapsed" desc="STAFF">
                LOG.info("=====================Beginning Staff Section=============================");
                String staffId = sms.getText().split("\\.")[1];
                LOG.info("Incoming Staff Id ::: " + staffId);
                //http://dev3.convergenceondemand.net/wfmservice/users/validatewithstaffcode/lk706
                StringBuilder sb = new StringBuilder(CONFIG.getWFMValidateURL());
                sb.append(staffId);
                LOG.info(sb.toString());
                try {
                    LOG.info("==================================================");
                    String retVal = HttpUtil.sendGet(sb.toString());
                    LOG.info(retVal);
                    LOG.info("==================================================");
                    StaffValidate sv = new Gson().fromJson(retVal, StaffValidate.class);
                    if (sv.isSuccess()) {
                        sd = sv.getData()[0];
                        sb = new StringBuilder("EKEDP Staff ID : ");
                        String jobTitle = sd.getJob_title() == null ? "" : sd.getJob_title();
                        sb.append(sd.getStaff_id()).append(" and Staff Code ").append(sd.getStaff_code()).append(" belongs to ").append(jobTitle).append(" ").append(sd.getName());
                        sb.append(". Status :: ").append(sd.getStatus()).append(". See more details here ").append(sd.getProfile_link());
                        returnMessage = sb.toString();
                    } else {
                        returnMessage = "Not found. Invalid Staff Id. Powered by EKEDP";
                    }
                    LOG.info(returnMessage);
                    LOG.info("=====================Finishing Staff Section=============================");
                } catch (Exception ex) {
                    returnMessage = "Invalid Staff Id. Powered by EKEDP";
                    LOG.info(returnMessage);
                    LOG.info("=====================Finishing Staff Section Exception Block=============================");
                }
                //</editor-fold>
                break;
            case "ALSD":
                //<editor-fold defaultstate="collapsed" desc="ALSD">
                //ALSD.APPROVALCODE.STAFFID
                //smsAcceptPowerSchedule/{approvalCode}/{staffId}/{msisdn}
                String approvalCode = sms.getText().split("\\.")[1];
                String sid = sms.getText().split("\\.")[2];
                //sb = new StringBuilder("http://172.29.11.87:8080/integration/feeder_auto");
                sb = new StringBuilder("http://172.29.10.130:8080/integration/smsAcceptPowerSchedule/");
                sb.append(approvalCode).append("/").append(sid).append("/").append(sms.getMsisdn());
                try {
                    String sendPost = HttpUtil.sendGet(sb.toString());
                    EMCCResponse respp = gson.fromJson(sendPost, EMCCResponse.class);
                    returnMessage = respp.getDesc();
                    LOG.info(sendPost);
                } catch (Exception ex) {
                    returnMessage = "Unrecognised SMS command. Please check your command is correct and try again. Thank you.";
                    ex.printStackTrace();
                }
                //</editor-fold>
                break;
            case "WFM":
                //<editor-fold defaultstate="collapsed" desc="WFM">
                StringBuilder wfmSb = new StringBuilder(CONFIG.getWFMValidateURL());
                wfmSb.append("0").append(sms.getMsisdn());
                LOG.info(wfmSb.toString());
                String retVal2;
                try {
                    retVal2 = HttpUtil.sendGet(wfmSb.toString());
                    LOG.info(retVal2);
                    StaffValidate sv = null;
                    try {
                        sv = new Gson().fromJson(retVal2, StaffValidate.class);
                        sd = sv.getData()[0];
                    } catch (JsonSyntaxException jsonSyntaxException) {
                        StaffValidateError sve = new Gson().fromJson(retVal2, StaffValidateError.class);
                        sv = new StaffValidate();
                        sv.setSuccess(sve.isSuccess());
                    }

                    LOG.info(sv.toString());
                    if (sv.isSuccess()) {
                        String wfm[] = sms.getText().split("\\.");
                        switch (wfm.length) {
                            case 1:
                                returnMessage = "Wrong syntax. Allowed syntax are WFM.TicketId and WFM.TicketId.Status";
                                break;
                            case 2://WFM.TicketId
                                sb = new StringBuilder(CONFIG.getWFMStatusList());
                                sb.append("/").append(wfm[1].trim());
                                sb.append("/").append("0").append(sms.getMsisdn());
                                LOG.info(sb.toString());
                                String message = HttpUtil.sendGet(sb.toString());
                                WFMResponse wresp = gson.fromJson(message, WFMResponse.class);
                                List<WFM> object = wresp.getObject();
                                if (object.isEmpty()) {
                                    //returnMessage = "No status list attached to TicketId " + wfm[1] + ". Powered by EKEDP.";
                                    returnMessage = "We cannot fetch statuses at the moment. Please try again later. Powered by EKEDP.";
                                } else {
                                    sb = new StringBuilder("Send WFM." + wfm[1] + ". ");
                                    int counter = 1;
                                    for (WFM w : object) {
                                        sb.append(counter).append("=").append(w.getName()).append(", ");
                                        counter++;
                                    }
                                    returnMessage = sb.toString();
                                }
                                break;
                            case 3://WFM.TicketId.Status
                                sb = new StringBuilder(CONFIG.getWFMStatusList());//LIVE
                                sb.append("/").append(wfm[1].trim());
                                sb.append("/").append(sms.getMsisdn());
                                LOG.info(sb.toString());
                                String respw = HttpUtil.sendGet(sb.toString());
                                WFMResponse wResp = gson.fromJson(respw, WFMResponse.class);
                                List<WFM> wfms = wResp.getObject();
                                if (wfms.isEmpty()) {
                                    returnMessage = "No status list attached to TicketId " + wfm[1] + ". Powered by EKEDP.";
                                } else {
                                    WFM myWfm = wfms.get(Integer.parseInt(wfm[2]) - 1);
                                    WFM2 w = new WFM2();
                                    w.setStatusToken(myWfm.getToken().trim());
                                    w.setStatusName(myWfm.getName().trim());
                                    w.setTicketId(Integer.parseInt(wfm[1].trim()));
                                    sb = new StringBuilder(CONFIG.getWFMUpdateStatus());
                                    sb.append("?phone=").append("0").append(sms.getMsisdn());
                                    LOG.info(sb.toString());
                                    String sendPost = HttpUtil.sendPost(sb.toString(), gson.toJson(w));
                                    LOG.info(sendPost);
                                    WFMResponse2 wfmResp = gson.fromJson(sendPost, WFMResponse2.class);
                                    List<WFM3> obj = wfmResp.getObject();
                                    if (obj.isEmpty()) {
                                        if (wfmResp.getDesc().startsWith("System")) {
                                            //returnMessage = "Please retry this " + sms.getText() + " request again with all spaces removed. Thank you.";
                                            returnMessage = "Please send Track." + wfm[1] + " to confirm the status of this ticket id. Thank you. Powered by EKEDP";
                                        } else if (wfmResp.getDesc().startsWith("Invalid")) {
                                            returnMessage = "This phone number is attached to more than one User. Please contact your system administrator. Thank you.";
                                        } else if (wfmResp.getDesc().startsWith("Successful")) {
                                            returnMessage = "Ticket #" + wfm[1].trim() + " has been updated to " + myWfm.getName().trim() + " status. Powered by EKEDP.";
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
                                }

                                break;
                        }
                    } else {
                        returnMessage = "Invalid Staff Phone Number. Powered by EKEDP";
                    }

                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(EkoDistribution.class.getName()).log(Level.SEVERE, null, ex);
                }
                //</editor-fold>
                break;
            case "METER":
                //<editor-fold defaultstate="collapsed" desc="METER">
                //METER.CS.METER_NUMBER.READING
                String phone = sms.getMsisdn();
                LOG.info("=====================Beginning Staff Section=============================");
                String meterNumber = sms.getText().split("\\.")[2];
                String meterReading = sms.getText().split("\\.")[3];
                LOG.info("Incoming Staff Phone ::: " + phone);
                //07089886646
                //StringBuilder sb2 = new StringBuilder("http://dev3.convergenceondemand.net/wfmservice/users/validatebyphone/");
                //StringBuilder sb2 = new StringBuilder("http://api-wfm.convergenceondemand.net/users/validatebyphone/");
                StringBuilder sb2 = new StringBuilder(CONFIG.getWFMValidateURL());
                //sb2.append("0").append(phone);
                sb2.append("0").append(phone);
                LOG.info(sb2.toString());
                try {
                    String retVal;
                    retVal = HttpUtil.sendGet(sb2.toString());
                    LOG.info("Meter Number ::: " + meterNumber);
                    LOG.info("Meter Reading ::: " + meterReading);
                    LOG.info(retVal);
                    StaffValidate sv = new Gson().fromJson(retVal, StaffValidate.class);
                    LOG.info(sv.toString());

                    if (sv.isSuccess()) {
                        sd = sv.getData()[0];
                        LOG.info("Starting EMCC call....");
                        sb2 = new StringBuilder(CONFIG.getSubmitMeterReadingURL());
                        emcc.setMeterNumber(meterNumber);
                        emcc.setMeterReading(meterReading);
                        emcc.setDistrict(sd.getDistricts());
                        emcc.setPhone(phone);
                        emcc.setName(sv.getData()[0].getName());
                        LOG.info(sb2.toString());
                        retVal = HttpUtil.sendPost(sb2.toString(), gson.toJson(emcc));
                        LOG.info(retVal);
                        eResp = new Gson().fromJson(retVal, EMCCResponse.class);
                        returnMessage = eResp.getDesc() + " Powered by EKEDP";
                    } else {
                        returnMessage = "Not found. Invalid Staff Phone Number. Powered by EKEDP";
                    }
                    LOG.info(returnMessage);
                    LOG.info("=====================Finishing Staff Section=============================");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    returnMessage = "Invalid Staff Id. Powered by EKEDP";
                    LOG.info(returnMessage);
                    LOG.info("=====================Finishing Staff Section Exception Block=============================");
                }
//</editor-fold>
                break;
            case "POWER":
                //<editor-fold defaultstate="collapsed" desc="POWER">
                String accountNumber = sms.getText().split("\\.")[1];
                sb = new StringBuilder(CONFIG.getEMCCFeederStatus());
                sb.append(accountNumber);
                FeederStatusResponse fs = null;
                try {
                    retVal2 = HttpUtil.sendGet(sb.toString());
                    fs = gson.fromJson(retVal2, FeederStatusResponse.class);
                    sb = new StringBuilder();
                    sb.append("Dear ").append(accountNumber).append(" your feeder ").append(fs.getObject().getLowVoltageFeederId().getName().trim());
                    //sb.append(" coming from ").append(fs.getObject().getInjectionSubstationId().getName().trim()).append(" Injection Substation");
                    sb.append(" is currently ").append(fs.getObject().getStatus()).append(". Last updated on ").append(fs.getObject().getUpdated());
                    if (fs.getObject() != null) {
                        FeederStatus f = fs.getObject();
                        if (f != null) {
                            if (f.getLowVoltageFeederId() != null) {
                                retVal2 = HttpUtil.sendGet(CONFIG.getEMCCCurrentSchedule() + f.getLowVoltageFeederId().getToken());
                                PowerScheduleResponse pss = gson.fromJson(retVal2, PowerScheduleResponse.class);
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
                    returnMessage = sb.toString();
                } catch (Exception e) {
                    return null;
                }
                //</editor-fold>
                break;
            default:
                //<editor-fold defaultstate="collapsed" desc="DEFAULT">

                //sb = new StringBuilder("http://172.29.11.87:8080/integration/feeder_auto");//Prod
                //sb = new StringBuilder("http://172.29.14.130:8080/integration/feeder_auto"); //Dev
                //sb = new StringBuilder("http://172.29.10.130:8080/integration/feeder_auto");//Staging
                EMCC e2 = new EMCC(sms.getText(), "0" + sms.getMsisdn());
                try {

                    HttpUtil.sendPost("http://172.29.10.130:8080/integration/feeder_auto", new Gson().toJson(e2));//Staging
                    String sendPost = HttpUtil.sendPost(CONFIG.getEMCCAlsd(), new Gson().toJson(e2));//Production
                    EMCCResponse respp = gson.fromJson(sendPost, EMCCResponse.class);
                    if (respp.getResp() == 0) {
                        returnMessage = "DONOTSENDMESSAGE";
                    } else {
                        returnMessage = respp.getDesc();
                    }
                    LOG.info(sendPost);
                } catch (Exception ex) {
                    returnMessage = "Unrecognised SMS command. Please check your command is correct and try again. Thank you.";
                    ex.printStackTrace();
                }

//                } else {
//                    returnMessage = "Unrecognised SMS command. Please check your command is correct and try again. Thank you.";
//                }
                //</editor-fold>
                break;
        }

        SOutgoing sOutgoing;
        if (returnMessage.length() > 250) {
            returnMessage = returnMessage.substring(0, 250);
            sOutgoing = new SOutgoing(sms, uniqueTransId, returnMessage);
            if (sd != null) {
                sOutgoing.setDepartment(sd.getDepartment());
                sOutgoing.setDistrict(sd.getDistricts());
                sOutgoing.setJobTitle(sd.getJob_title());
                sOutgoing.setStaffCode(sd.getStaff_code());
                sOutgoing.setStaffId(sd.getStaff_id());
                sOutgoing.setStaffName(sd.getName());
                sOutgoing.setStatus(sd.getStatus());
            }
            accessbean.create(sOutgoing);
        } else {
            sOutgoing = new SOutgoing(sms, uniqueTransId, returnMessage);
            if (sd != null) {
                sOutgoing.setDepartment(sd.getDepartment());
                sOutgoing.setDistrict(sd.getDistricts());
                sOutgoing.setJobTitle(sd.getJob_title());
                sOutgoing.setStaffCode(sd.getStaff_code());
                sOutgoing.setStaffId(sd.getStaff_id());
                sOutgoing.setStaffName(sd.getName());
                sOutgoing.setStatus(sd.getStatus());
            }
            accessbean.create(sOutgoing);
        }

        return returnMessage;
    }
}
