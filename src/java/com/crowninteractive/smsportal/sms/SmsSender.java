/*
 * Crown Interactive. Proprietary.
 */
package com.crowninteractive.smsportal.sms;

import com.crowninteractive.smsportal.util.HttpUtil;
import java.net.URLEncoder;

/**
 * In charge of sending SMS.
 *
 * @author oladeji
 * @author charlee
 */
public final class SmsSender {

    private static String SMSC = "mtech";
    private static final String HOST = "localhost";
    private static final int PORT = 28080;
    private static final int TIMEOUT = 20; // Timeout in seconds.

    private SmsSender() {
        super();
    }

    /**
     * Sends a SMS using the specified parameters.
     *
     * @param destination recipient
     * @param source sender
     * @param text message body
     * @return status
     */
    public static String send(String destination, String source, String text)
            throws IllegalArgumentException, Exception {

        if (destination.equalsIgnoreCase("UNKNOWN")) {
            return "Message not sent to " + destination;
        } else if (destination.contains("-")) {
            return "Message not sent to " + destination;
        } else if (destination.contains(",")) {
            String[] split = destination.split(",");
            for (String s : split) {
                send(s, source, text);
            }
            return "Message sent";
        } else if (destination.contains(" ")) {
            destination = destination.replaceAll(" ", "");
        } else if (destination.length() > 15) {
            return destination + " as destination phone number is too long!";
        } else {
            destination = destination.trim();
        }

        if (destination.isEmpty()) {
            return "Message not sent to empty phone number";
        }
        StringBuilder sb = new StringBuilder("http://" + HOST + ":" + PORT);
        sb.append("/adapter/sendsms/");
        sb.append("?destination=").append(encode(destination));
        if (text.length() > 254) {
            sb.append("&text=").append(encode(text.substring(0, 253)));
        } else {
            sb.append("&text=").append(encode(text));
        }

        /**
         * Externalize shortcode against SMSC
         */
        switch (source) {
            case "20120":
                source = "20120";
                SMSC = "vas2net";
                break;
            case "7827":
                source = "7827";
                SMSC = "mtech";
                break;
            case "55999":
                source = "55999";
                SMSC = "mtech";
                break;
            case "EKEDP":
            case "CICOD":
                SMSC = "crownbulk";
                break;
            case "PAYCONVENIENCE":
                source = "CICOD";
                SMSC = "crownbulk";
                break;
            default:
                source = "55999";
                SMSC = "mtech";
                break;
        }
        sb.append("&source=").append(encode(source));
        sb.append("&smscid=").append(encode(SMSC));
        return fetch(sb.toString());
    }

    private static String encode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (Exception ex) {
            // Log event.

            throw new RuntimeException(ex);
        }
    }

    private static String fetch(String str) throws Exception {
        return HttpUtil.sendGet(str);
    }
}
