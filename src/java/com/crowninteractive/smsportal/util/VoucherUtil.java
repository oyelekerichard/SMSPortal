/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.util;

import com.crowninteractive.smsportal.dto.BaseResponse;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Adekanmbi Oluremi adekanmbi.oluremi@gmail.com +2348098753155
 */
public class VoucherUtil {

    private final static String WSDLURL = Config.getInstance().getUCGVoucherURL();
    //private final static String WSDLURL = "http://81.26.64.34:8080/UCG/Voucher/Endpoint?wsdl";
    private static final Logger LOG = Logger.getLogger(VoucherUtil.class);

    public static String newMethod() {
        String xml = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.voucher.ucg.convergenceondemand.net/\">\n"
                + "    <x:Header>\n"
                + "        <ws:tenantId>EKEDP</ws:tenantId>\n"
                + "    </x:Header>\n"
                + "    <x:Body>\n"
                + "        <ws:loadVoucher>\n"
                + "            <pin>137264645018</pin>\n"
                + "            <params>\n"
                + "                <amount>0</amount>\n"
                + "                <extraData>\n"
                + "                    <entry>\n"
                + "                        <key>accountNumber</key>\n"
                + "                        <value>0121061863-01</value>\n"
                + "                    </entry>\n"
                + "                </extraData>\n"
                + "                <summary>Bill payment</summary>\n"
                + "            </params>\n"
                + "        </ws:loadVoucher>\n"
                + "    </x:Body>\n"
                + "</x:Envelope>";

        return "";
    }

    public static BaseResponse processDetails(String phone, String accountIdOrOrderId, String voucherPin, String type) {
        BaseResponse resp = new BaseResponse();
        try {
            switch (type) {
                case "A":
                    resp = getXMLAccountId(phone, accountIdOrOrderId, voucherPin);
                    break;
                case "R":
                    resp = getXMLReconnect(phone, accountIdOrOrderId, voucherPin);
                    break;
                default:
                    resp = getXMLMeterNumber(phone, accountIdOrOrderId, voucherPin);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private static BaseResponse getXMLAccountId(String phone, String accountId, String voucherPin) {
        BaseResponse resp = new BaseResponse();
        try {
            String getXml = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.voucher.ucg.convergenceondemand.net/\">\n"
                    + "    <x:Header>\n"
                    + "        <ws:tenantId>EKEDP</ws:tenantId>\n"
                    + "    </x:Header>\n"
                    + "    <x:Body>\n"
                    + "        <ws:loadVoucher>\n"
                    + "            <pin>" + voucherPin + "</pin>\n"
                    + "            <params>\n"
                    + "                <amount>0</amount>\n"
                    + "                <extraData>\n"
                    + "                    <entry>\n"
                    + "                        <key>accountNumber</key>\n"
                    + "                        <value>" + accountId + "</value>\n"
                    + "                    </entry>\n"
                    + "               <entry>\n"
                    + "                  <key>userId</key>\n"
                    + "                  <value>" + phone + "</value>\n"
                    + "               </entry>\n"
                    + "               <entry>\n"
                    + "                  <key>phone</key>\n"
                    + "                  <value>" + getMSISDN(phone) + "</value>\n"
                    + "               </entry>\n"
                    + "               <entry>\n"
                    + "                  <key>tenantId</key>\n"
                    + "                  <value>EKEDP</value>\n"
                    + "               </entry>\n"
                    + "                </extraData>\n"
                    + "                <summary>Bill payment</summary>\n"
                    + "            </params>\n"
                    + "        </ws:loadVoucher>\n"
                    + "    </x:Body>\n"
                    + "</x:Envelope>";
            System.out.println(getXml);
            String postXML = HttpUtil.postXML(WSDLURL, getXml);
            resp = convertVoucherResp(postXML);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private static BaseResponse getXMLMeterNumber(String phone, String accountId, String voucherPin) {
        BaseResponse resp = new BaseResponse();
        try {
            String getXml = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.voucher.ucg.convergenceondemand.net/\">\n"
                    + "    <x:Header>\n"
                    + "        <ws:tenantId>EKEDP</ws:tenantId>\n"
                    + "    </x:Header>\n"
                    + "    <x:Body>\n"
                    + "        <ws:loadVoucher>\n"
                    + "            <pin>" + voucherPin + "</pin>\n"
                    + "            <params>\n"
                    + "                <amount>0</amount>\n"
                    + "                <extraData>\n"
                    + "                    <entry>\n"
                    + "                        <key>meterNumber</key>\n"
                    + "                        <value>" + accountId + "</value>\n"
                    + "                    </entry>\n"
                    + "               <entry>\n"
                    + "                  <key>userId</key>\n"
                    + "                  <value>" + phone + "</value>\n"
                    + "               </entry>\n"
                    + "               <entry>\n"
                    + "                  <key>phone</key>\n"
                    + "                  <value>" + getMSISDN(phone) + "</value>\n"
                    + "               </entry>\n"
                    + "               <entry>\n"
                    + "                  <key>tenantId</key>\n"
                    + "                  <value>EKEDP</value>\n"
                    + "               </entry>\n"
                    + "                </extraData>\n"
                    + "                <summary>Bill payment</summary>\n"
                    + "            </params>\n"
                    + "        </ws:loadVoucher>\n"
                    + "    </x:Body>\n"
                    + "</x:Envelope>";

            String postXML = HttpUtil.postXML(WSDLURL, getXml);
            resp = convertVoucherResp(postXML);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private static BaseResponse getXMLReconnect(String phone, String accountId, String voucherPin) {
        BaseResponse resp = new BaseResponse();
        try {
            String getXml = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.voucher.ucg.convergenceondemand.net/\">\n"
                    + "    <x:Header>\n"
                    + "        <ws:tenantId>EKEDP</ws:tenantId>\n"
                    + "    </x:Header>\n"
                    + "    <x:Body>\n"
                    + "        <ws:loadVoucher>\n"
                    + "            <pin>" + voucherPin + "</pin>\n"
                    + "            <params>\n"
                    + "                <amount>0</amount>\n"
                    + "                <extraData>\n"
                    + "                    <entry>\n"
                    + "                        <key>orderId</key>\n"
                    + "                        <value>" + accountId + "</value>\n"
                    + "                    </entry>\n"
                    + "               <entry>\n"
                    + "                  <key>userId</key>\n"
                    + "                  <value>" + phone + "</value>\n"
                    + "               </entry>\n"
                    + "               <entry>\n"
                    + "                  <key>phone</key>\n"
                    + "                  <value>" + getMSISDN(phone) + "</value>\n"
                    + "               </entry>\n"
                    + "               <entry>\n"
                    + "                  <key>tenantId</key>\n"
                    + "                  <value>EKEDP</value>\n"
                    + "               </entry>\n"
                    + "                </extraData>\n"
                    + "                <summary>Bill payment</summary>\n"
                    + "            </params>\n"
                    + "        </ws:loadVoucher>\n"
                    + "    </x:Body>\n"
                    + "</x:Envelope>";

            String postXML = HttpUtil.postXML(WSDLURL, getXml);
            resp = convertVoucherResp(postXML);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private static BaseResponse convertVoucherResp(String responseXml) {
        System.out.println(responseXml);
        BaseResponse resp = new BaseResponse();
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(responseXml));
            Document doc = db.parse(is);

            NodeList resultCode = doc.getElementsByTagName("code");
            Node rc = resultCode.item(0);
            String responseCode = rc.getTextContent();
            resp.setRetn(Integer.parseInt(responseCode));

            NodeList description = doc.getElementsByTagName("description");
            Node drc = description.item(0);
            String desc = drc.getTextContent();
            resp.setDesc(desc);

            NodeList entity = doc.getElementsByTagName("entity");
            Node rcentity = entity.item(0);
            String responsercentity = rcentity.getTextContent();
            resp.setObj(responsercentity);

        } catch (ParserConfigurationException ex) {
        } catch (SAXException ex) {
        } catch (IOException ex) {
        } catch (NullPointerException ex) {
        }
        return resp;
    }

    private static String getMSISDN(String msisdn) {
        StringBuilder sb = new StringBuilder("0");
        if (msisdn.startsWith("0")) {
            return sb.append(msisdn.substring(1)).toString();
        }
        if (msisdn.startsWith("234")) {
            return sb.append(msisdn.substring(3)).toString();
        }
        return sb.append(msisdn).toString();
    }

}
