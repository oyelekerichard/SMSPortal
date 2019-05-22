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
    private static final String HOST = "172.29.11.17";
    private static final int PORT = 8080;
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
//        if (Inputs.hasBlank(destination, source, text)) {
//            // Log event.
//            throw new IllegalArgumentException("Inputs cannot contain blanks.");
//        }

        StringBuilder sb = new StringBuilder("http://" + HOST + ":" + PORT);
        sb.append("/adapter/sendsms/");
        sb.append("?destination=").append(encode(destination));
        if (!source.equals("EKEDP") && !source.equals("55999")) {
            source = "CICOD";
        }
        sb.append("&source=").append(encode(source));
        sb.append("&text=").append(encode(text));

        /**
         * Externalize shortcode against SMSC
         */
        switch (source) {
            case "20120":
                SMSC = "vas2net";
                break;
            case "7827":
                SMSC = "mtech";
                break;
            case "55999":
                SMSC = "mtech";
                break;
            case "EKEDP":
            case "CICOD":
                SMSC = "crownbulk";
                break;
            default:
                SMSC = "mtech";
                break;
        }
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
