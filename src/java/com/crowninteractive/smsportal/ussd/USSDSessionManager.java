package com.crowninteractive.smsportal.ussd;

import com.crowninteractive.smsportal.interswitch.job.DLRReportJob;
import com.crowninteractive.smsportal.interswitch.job.DRReportJobForEnrouteStatus;
import java.util.Hashtable;
import java.util.Map;

public class USSDSessionManager {

    private static Hashtable<String, USSDSession> HTABLE = new Hashtable<String, USSDSession>();
    private static final USSDSessionManager USSDMGR = new USSDSessionManager();
    //private static final Logger LOG = Logger.getLogger(USSDSessionManager.class);

    //Attention: Do not instatiate this guy directly ever. Always use getInstance();
    private USSDSessionManager() {
        new Thread(new SessionCleaner()).start();
        new Thread(new DLRReportJob()).start();
        new Thread(new DRReportJobForEnrouteStatus()).start();
    }

    public static USSDSessionManager getInstance() {
        return USSDMGR;
    }

    public void add(USSDSession session) {
        HTABLE.put(session.getSessionId(), session);
    }

    public void destroySession(String id) {
        HTABLE.remove(id);
        System.out.println("Session " + id + " was forcefully destroyed after process completed...");
    }

    public USSDSession getSession(String sessionId) {
        return HTABLE.get(sessionId);
    }

    public void destroyAllSessions() {
        for (Map.Entry<String, USSDSession> entry : HTABLE.entrySet()) {
            String key = entry.getKey();
            HTABLE.remove(key);
        }
    }

    public boolean previousExist(String msisdn) {
        boolean exist = false;
        for (Map.Entry<String, USSDSession> entry : HTABLE.entrySet()) {
            String key = entry.getKey();
            USSDSession session = entry.getValue();
            if (session.getMsisdn().equals(msisdn)) {
                exist = true;
            }
        }
        return exist;
    }

    public USSDSession checkForPreviousSession(String msisdn) {
        USSDSession queueDetails = null;
        for (Map.Entry<String, USSDSession> entry : HTABLE.entrySet()) {
            String key = entry.getKey();
            USSDSession session = entry.getValue();
            if (session.getMsisdn().equals(msisdn)) {
                queueDetails = session;
            }
        }
        return queueDetails;
    }

    public class SessionCleaner implements Runnable {

        public void run() {
            System.out.println("Session Cleaner Started...");
            java.util.Enumeration<String> keys = null;
            while (true) {
                try {
                    keys = HTABLE.keys();
                    while (keys.hasMoreElements()) {
                        String id = keys.nextElement();
                        USSDSession session = HTABLE.get(id);
                        if (session.getProvider().contains("B*")) {
                            if (session.getElapsed() > (10 * 60000)) {
                                System.out.println("Session " + id + " was destroyed by cleaner after expiry....");
                                session.setSessionIsAlive(false);
                                destroySession(session.getSessionId());
                            }
                        } else if (session.getElapsed() > (60 * 60000)) {
                            System.out.println("Session " + id + " was destroyed by cleaner after expiry....");
                            session.setSessionIsAlive(false);
                            destroySession(session.getSessionId());
                        }

                    }
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

    }

}
