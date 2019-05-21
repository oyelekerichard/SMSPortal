/*
 * Crown Interactive. Proprietary.
 */
package com.crowninteractive.smsportal.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages response codes.
 *
 * @author oladeji
 */
public class ResponseCodes {

    //<editor-fold defaultstate="collapsed" desc="FIELDS and CONSTANTS">
    public static final int REQUEST_SUCCESSFUL = 0;
    public static final int INVALID_PHONE_NUMBER = 100;
    public static final int INVALID_EMAIL = 101;
    public static final int BAD_INPUT_PARAM = 102;
    public static final int CODE_ALREADY_EXIST = 103;
    public static final int INVALID_ACCOUNT = 200;
    public static final int ACCOUNT_ALREADY_EXISTS = 201;
    public static final int ACCOUNT_DOES_NOT_EXIST = 202;
    public static final int ACCESS_DENIED = 203;
    public static final int PASSWORD_MISMATCH = 204;
    public static final int INVALID_CREDENTIALS = 205;
    public static final int WALLET_NOT_FOUND = 206;
    public static final int ACCOUNT_DISABLED = 207;
    public static final int EMAIL_ALREADY_EXISTS = 208;
    public static final int ACCOUNT_AUTHENTICATION_ERROR = 209;
    public static final int ACCOUNT_UPDATE_FAILED = 210;
    public static final int DATA_CONNECTION_ERROR = 900;
    public static final int REQUESTER_AUTH_ERROR = 901;
    public static final int INTERNAL_SYSTEM_ERROR = 902;
    public static final int PERSISTENCE_ERROR = 904;
    public static final int SERVICE_ERROR = 905;
    public static final int CONSTRAINT_VIOLATION_ERROR = 906;
    public static final int EMPTY_RECORDS = 907;
    public static final int OTHER_ERROR = 910;
    private static final Map<Integer, String> CODE_MESSAGE_MAP;
    //</editor-fold>

    static {
        CODE_MESSAGE_MAP = new HashMap<>();
        CODE_MESSAGE_MAP.put(REQUEST_SUCCESSFUL, "Request successful!");
        CODE_MESSAGE_MAP.put(CODE_ALREADY_EXIST, "Shortcode already exist!");
        CODE_MESSAGE_MAP.put(INVALID_PHONE_NUMBER, "Invalid phone number");
        CODE_MESSAGE_MAP.put(INVALID_EMAIL, "Invalid email");
        CODE_MESSAGE_MAP.put(BAD_INPUT_PARAM, "Bad input parameter");

        CODE_MESSAGE_MAP.put(INVALID_ACCOUNT, "Invalid user account");
        CODE_MESSAGE_MAP.put(ACCOUNT_ALREADY_EXISTS, "Account already exists");
        CODE_MESSAGE_MAP.put(ACCOUNT_DOES_NOT_EXIST, "Account does not exist");
        CODE_MESSAGE_MAP.put(ACCESS_DENIED, "You do not have access to data for this page!");
        CODE_MESSAGE_MAP.put(PASSWORD_MISMATCH, "Password mismatch");
        CODE_MESSAGE_MAP.put(INVALID_CREDENTIALS, "Invalid credentials");
        CODE_MESSAGE_MAP.put(WALLET_NOT_FOUND, "Wallet not found");
        CODE_MESSAGE_MAP.put(ACCOUNT_DISABLED, "The account is currently disabled. Please contact Big Games.");
        CODE_MESSAGE_MAP.put(EMAIL_ALREADY_EXISTS, "Email already exists");
        CODE_MESSAGE_MAP.put(ACCOUNT_AUTHENTICATION_ERROR, "Your account could not be authenticated. Please try again.");
        CODE_MESSAGE_MAP.put(ACCOUNT_UPDATE_FAILED, "Account update failed. Please try again!");
        CODE_MESSAGE_MAP.put(EMPTY_RECORDS, "No record found!");
        CODE_MESSAGE_MAP.put(INTERNAL_SYSTEM_ERROR, "Unable to process request at the moment. Please try again later.");
        CODE_MESSAGE_MAP.put(DATA_CONNECTION_ERROR, "Loss of data connection. Please try again.");
        CODE_MESSAGE_MAP.put(REQUESTER_AUTH_ERROR, "Requester authorization error");
        CODE_MESSAGE_MAP.put(SERVICE_ERROR, "Unable to process request at the moment. Please try again later.");
        CODE_MESSAGE_MAP.put(OTHER_ERROR, "Other error");
        CODE_MESSAGE_MAP.put(PERSISTENCE_ERROR, "Your request can not be could not be processed. Please try again later.");
        CODE_MESSAGE_MAP.put(CONSTRAINT_VIOLATION_ERROR, "An identifier you have used in your request already exist!");
    }

    private ResponseCodes() {
    }

    /**
     * Fetches the corresponding default description for a response code.
     * Nullable.
     */
    public static String getDefaultMessageFor(int code) {
        return CODE_MESSAGE_MAP.get(code);
    }
}
