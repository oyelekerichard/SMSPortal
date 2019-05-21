/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.notification;

import com.crowninteractive.smsportal.dto.Notification;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Oluremi Adekanmbi
 * <oluremi.adekanmbi@etranzact.com>
 */
public class MailerHelper {

    private final static Logger L = Logger.getLogger(MailerHelper.class);

    public static void main(String[] args) {
        try {
            Notification dummy = dummy();
            Map map = generateReceiptMap(dummy);
            String htmlString = getHTMLString(map, getFTLName("LOGIN"));
            L.info(htmlString);
            Mailer.sendSSLMessage(dummy.getUserEmail(), "Porplecom Login", htmlString, "Porplecom");
            htmlString = getHTMLString(map, getFTLName("AIRTIME"));
            L.info(htmlString);
            Mailer.sendSSLMessage(dummy.getUserEmail(), "Porplecom Airtime Purchase", htmlString, "Porplecom");
            htmlString = getHTMLString(map, getFTLName("ACCOUNT_CREATION"));
            L.info(htmlString);
            Mailer.sendSSLMessage(dummy.getUserEmail(), "Porplecom Account Creation", htmlString, "Porplecom");
        } catch (Exception exception) {
        }
    }

    public static Notification dummy() {
        Notification n = new Notification();
        n.setAmount("100.00");
        n.setBeneficiary("08098753155");
        n.setDateOfTransaction(DateTimeUtil.getShortDate(new Date()));
        n.setLoginDateTime(DateTimeUtil.getShortDate(new Date()));
        n.setFullName("Adekanmbi Oluremi");
        n.setProvider("Acc");
        n.setProviderRef("FT23578637293");
        n.setTransactionRef("25746847394837");
        n.setUserEmail("adekanmbi.oluremi@gmail.com");
        n.setPassword("@JamesDeen1982");

        return n;
    }

    public static boolean sendEmail(Notification notif, String subject, String type, String toUser) {
        try {
            String from = "paymentalerts43";
            String pass = "@Paymentalerts43";
            String[] to = {toUser}; // list of recipient email addresses
            Map map = generateReceiptMap(notif);
            String htmlString = getHTMLString(map, getFTLName(type));
            StringBuilder sb = new StringBuilder();
            sb.append(notif.getProvider()).append("");
            return Main.sendFromGMail(from, pass, to, subject, htmlString);
            //return Mailer.sendSSLMessage(toUser, subject, htmlString, "porplecom");
        } catch (Exception ex) {
            return false;
        }
    }

    public static Map generateReceiptMap(Notification p) {
        Map<String, Object> data = new HashMap<>();
        data.put("provider", p.getProvider() == null ? "" : p.getProvider());
        data.put("dateOfTransaction", p.getDateOfTransaction() == null ? "" : p.getDateOfTransaction());
        data.put("loginDateTime", p.getLoginDateTime() == null ? "" : p.getLoginDateTime());
        data.put("fullName", p.getFullName() == null ? "" : p.getFullName());
        data.put("providerRef", p.getProviderRef() == null ? "" : p.getProviderRef());
        data.put("transactionRef", p.getTransactionRef() == null ? "" : p.getTransactionRef());
        data.put("amount", p.getAmount() == null ? "" : p.getAmount());
        data.put("beneficiary", p.getBeneficiary() == null ? "" : p.getBeneficiary());
        data.put("userEmail", p.getUserEmail() == null ? "" : p.getUserEmail());
        data.put("link", p.getLink() == null ? "" : p.getLink());
        data.put("password", p.getPassword() == null ? "" : p.getPassword());
        data.put("resellerCode", p.getResellerCode() == null ? "Not Applicable" : p.getResellerCode());
        return data;
    }

    public static String getHTMLString(Map data, String ftlName) {
        String html = "";
        L.info("Loading " + ftlName + " template");
        Configuration cfg = new Configuration();
        try {
            cfg.setClassForTemplateLoading(Notification.class, "/");
            Template template = cfg.getTemplate(ftlName + ".ftl");
            StringWriter sw = new StringWriter();
            template.process(data, sw);
            html = sw.toString();
        } catch (IOException e) {
            L.info("IOException");
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return html;
    }

    public static String getFTLName(String type) {
        switch (type.toUpperCase()) {
            case "ACCOUNT_CREATION":
                return "accountcreation";
            default:
                return "unknown";
        }
    }
}
