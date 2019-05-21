/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.apache.log4j.Logger;

/**
 *
 * @author Oluremi Adekanmbi
 * <oluremi.adekanmbi@etranzact.com>
 */
public class HttpUtil {

    private final static String USER_AGENT = "Mozilla/5.0";
    private final Logger L = Logger.getLogger(HttpUtil.class);

    public static String sendGet(String url) throws Exception {
        System.out.println(url);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");
        con.setReadTimeout(10000);

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Response in HttpUtil ---->  " + response.toString());
        return response.toString();
    }

    public static String sendPost(String url, String jsonString) throws Exception {
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + jsonString);
        URL obj = new URL(url);
        //HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(jsonString);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        return response.toString();

    }

    public static String postXML(String soapurl, String xml) throws Exception {
        HttpURLConnection connection = null;
        OutputStreamWriter wout = null;
        BufferedReader br = null;
        String line;
        try {
            URL u = new URL(soapurl);
            HttpURLConnection urlc = (HttpURLConnection) u.openConnection();
            urlc.setUseCaches(false);
            urlc.setRequestProperty("User-Agent", "Polaris/1.4.1");
            urlc.setRequestProperty("Connection", "Keep-Alive");
            urlc.setConnectTimeout(5000);
            urlc.setReadTimeout(6 * 1000);
            URLConnection uc = u.openConnection();
            connection = (HttpURLConnection) uc;
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("Content-Length", String.valueOf(xml.length()));
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            wout = new OutputStreamWriter(connection.getOutputStream());
            wout.write(xml);
            wout.close();
            StringBuilder sb = new StringBuilder();
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case 200:
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    break;
                default:
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    break;
            }
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            close(wout);
            close(br);
        }

    }

    public static void close(Closeable cls) {
        try {
            if (cls != null) {
                cls.close();
            }
        } catch (Exception ex) {
        }
    }
}
