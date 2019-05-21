package com.crowninteractive.smsportal.ussd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class USSDSession {

    private String sessionId;
    private String msisdn;
    private String provider;
    private boolean sessionIsAlive = true;
    private String accountNumber;
    private String[] expectedDetails;
    private int nextPosition = 0;
    private int pagePosition = 0;
    private int menuPosition = 0;
    private String currentSection;
    private String response;
    private final long currentTime = System.currentTimeMillis();
    private List<String> queue = new ArrayList<String>();
    private Map<String, Object> sessionData = new HashMap<String, Object>();

    public USSDSession(String mobile, String sessionId) {
        this.msisdn = mobile;
        this.sessionId = sessionId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getCurrentSection() {
        return currentSection;
    }

    public void setCurrentSection(String currentSection) {
        this.currentSection = currentSection;
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    public void setMenuPosition(int menuPosition) {
        this.menuPosition = menuPosition;
    }

    public Map<String, Object> getSessionData() {
        return sessionData;
    }

    public void setSessionData(Map<String, Object> sessionData) {
        this.sessionData = sessionData;
    }

    public int getPagePosition() {
        return pagePosition;
    }

    public void setPagePosition(int pagePosition) {
        this.pagePosition = pagePosition;
    }

    public boolean isSessionIsAlive() {
        return sessionIsAlive;
    }

    public void setSessionIsAlive(boolean sessionIsAlive) {
        this.sessionIsAlive = sessionIsAlive;
    }

    public String[] getExpectedDetails() {
        return expectedDetails;
    }

    public void setExpectedDetails(String[] expectedDetails) {
        this.expectedDetails = expectedDetails;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<String> getQueue() {
        return queue;
    }

    public void addTo(String str) {
        queue.add(str);
    }

    long getElapsed() {
        return System.currentTimeMillis() - currentTime;
    }

    public void getQueue(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
