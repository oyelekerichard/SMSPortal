/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.service;

import com.crowninteractive.smsportal.model.MenuItem;
import com.crowninteractive.smsportal.model.USSDTemplate;
import com.crowninteractive.smsportal.model.UssdTransaction;
import com.crowninteractive.smsportal.model.UssdTransactionLog;
import com.crowninteractive.smsportal.model.dto.GenericCount;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author adekanmbi
 */
public class UssdTxnServiceImpl {

    private static UssdTxnServiceImpl UINSTANCE = new UssdTxnServiceImpl();
    private final MasterUSSDServiceImpl MINSTANCE = MasterUSSDServiceImpl.getInstance();
    private final DBAccessBean accessbean;
    private static final Logger L = Logger.getLogger(UssdTxnServiceImpl.class);

    private UssdTxnServiceImpl() {
        accessbean = new DBAccessBean("SMSPortalPU");
    }

    public static UssdTxnServiceImpl getInstance() {
        if (UINSTANCE == null) {
            UINSTANCE = new UssdTxnServiceImpl();
        }
        return UINSTANCE;
    }

    public List<UssdTransaction> findTxnBySessionId(String sessionid) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<UssdTransaction> q = em.createNamedQuery("UssdTransaction.findBySessionid", UssdTransaction.class);
            q.setParameter("sessionid", sessionid);
            List<UssdTransaction> resultList = q.getResultList();
            return resultList;
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public UssdTransaction createTxn(String msisdn, String sessionId, String incoming, String reference, String network) {
        UssdTransaction txn = new UssdTransaction();
        txn.setMsisdn(msisdn);
        txn.setSessionid(sessionId);
        txn.setReference(reference);
        txn.setNetwork(network);
        txn.setIncomingMessage(incoming);
        txn.setIncomingTime(DateTimeUtil.getCurrentDate());
        accessbean.create(txn);
        return txn;
    }

    public UssdTransaction updateTxn(String reference, String outgoing) {
        UssdTransaction txn = findLastByReference(reference);
        txn.setOutgoingMessage(outgoing);
        txn.setOutgoingTime(DateTimeUtil.getCurrentDate());
        txn = accessbean.merge(txn);
        return txn;
    }

    public UssdTransaction updateTxn(String reference, String outgoing, String staffName, String staffId, String district, String staffCode, String status) {
        UssdTransaction txn = findLastByReference(reference);
        txn.setOutgoingMessage(outgoing);
        txn.setStaffName(staffName);
        txn.setStaffId(staffId);
        txn.setStaffCode(staffCode);
        txn.setDistrict(district);
        txn.setStatus(status);
        txn.setOutgoingTime(DateTimeUtil.getCurrentDate());
        txn = accessbean.merge(txn);
        return txn;
    }

    public UssdTransaction findByTxnId(Integer id) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<UssdTransaction> q = em.createNamedQuery("UssdTransaction.findById", UssdTransaction.class);
            q.setParameter("id", id);
            UssdTransaction txn = q.getSingleResult();
            return txn;
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public UssdTransaction findByReference(String reference) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<UssdTransaction> q = em.createNamedQuery("UssdTransaction.findByReference", UssdTransaction.class);
            q.setParameter("reference", reference);
            UssdTransaction txn = q.getSingleResult();
            return txn;
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public UssdTransaction findLastByReference(String reference) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<UssdTransaction> q = em.createNamedQuery("UssdTransaction.findByReference", UssdTransaction.class);
            q.setParameter("reference", reference);
            List<UssdTransaction> txns = (List<UssdTransaction>) q.getResultList();
            return txns.get(txns.size() - 1);
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<UssdTransaction> findDistinctTransactions(int page, int size) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<UssdTransaction> q = em.createNamedQuery("UssdTransaction.findDistinctTransaction", UssdTransaction.class);
            q.setFirstResult(page * size);
            q.setMaxResults(size);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<UssdTransaction> findDistinctUsers() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<UssdTransaction> q = em.createNamedQuery("UssdTransaction.findDistinctUsers", UssdTransaction.class);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<UssdTransaction> findDistinctUsers(Date startDate, Date endDate) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<UssdTransaction> q = em.createNamedQuery("UssdTransaction.findDistinctUsersByDate", UssdTransaction.class);
            q.setParameter("startDate", startDate);
            q.setParameter("endDate", endDate);
            if (q.getResultList() != null) {
                return q.getResultList();
            }
            return new ArrayList<UssdTransaction>();

        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<UssdTransaction> findDistinctTransactions() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<UssdTransaction> q = em.createNamedQuery("UssdTransaction.findDistinctTransaction", UssdTransaction.class);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<UssdTransaction> findDistinctTransactions(Date startDate, Date endDate) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<UssdTransaction> q = em.createNamedQuery("UssdTransaction.findDistinctTransactionByDate", UssdTransaction.class);
            q.setParameter("startDate", startDate);
            q.setParameter("endDate", endDate);
            q.setMaxResults(20);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<UssdTransaction> findDistinctTransactions2(Date startDate, Date endDate) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<UssdTransaction> q = em.createNamedQuery("UssdTransaction.findDistinctTransactionByDate", UssdTransaction.class);
            q.setParameter("startDate", startDate);
            q.setParameter("endDate", endDate);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<UssdTransaction> findDistinctTransactions3(Date startDate, Date endDate) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "SELECT u FROM UssdTransaction u WHERE u.incomingTime BETWEEN :startDate AND :endDate";
            TypedQuery<UssdTransaction> q = em.createQuery(sql, UssdTransaction.class);
            q.setParameter("startDate", startDate);
            q.setParameter("endDate", endDate);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<UssdTransaction> findDistinctTransactions2(Date startDate, Date endDate, int page, int size) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<UssdTransaction> q = em.createNamedQuery("UssdTransaction.findDistinctTransactionByDate", UssdTransaction.class);
            q.setParameter("startDate", startDate);
            q.setParameter("endDate", endDate);
            q.setFirstResult(page).setMaxResults(size);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<USSDTemplate> findDistinctUSSDTemplates() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<USSDTemplate> q = em.createNamedQuery("USSDTemplate.findAll", USSDTemplate.class);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public USSDTemplate findTemplate(Long id) {
        return accessbean.findSingle(USSDTemplate.class, id);
    }

    public MenuItem findMenuItem(Long id) {
        return accessbean.findSingle(MenuItem.class, id);
    }

    public List<GenericCount> findUniqueUSSDCount(Date startDate, Date endDate) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            Query q = em
                    .createNativeQuery("select u.id, u.incoming_message as name, count(*) as count from ussd_transaction u where "
                            + "u.incoming_message like '*%' and u.incoming_time "
                            + "between ?1 and ?2 "
                            + "GROUP BY u.incoming_message ORDER BY u.incoming_time DESC;", GenericCount.class);
            q.setHint("javax.persistence.cache.storeMode", "REFRESH");
            q.setParameter(1, startDate);
            q.setParameter(2, endDate);
            q.setMaxResults(100);
            GenericCount g;
            List<GenericCount> retVals = new ArrayList<GenericCount>();
            List<GenericCount> gcs = (List<GenericCount>) q.getResultList();
            L.info("Size ::: " + gcs.size());
            for (GenericCount gc : gcs) {
                MenuItem menu = MINSTANCE.findMenuWithIncoming(gc.getName());
                if (menu == null) {
                    String styledShortCode = MasterUSSDServiceImpl.getShortCodeStyle(gc.getName());
                    menu = MINSTANCE.findMenuWithIncoming(styledShortCode);
                }
                if (menu != null) {
                    g = new GenericCount(menu.getShortCode(), 1);
                    if (retVals.contains(g)) {
                        int indexOf = retVals.indexOf(g);
                        GenericCount get = retVals.get(indexOf);
                        get.setCount(get.getCount() + 1);
                    } else {
                        retVals.add(new GenericCount(menu.getShortCode(), 1));
                    }
                }
            }
            L.info(">>>>>>>>>>>>>>>>>>>>>>>> " + retVals);
            return retVals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public List<UssdTransaction> search(String searchString) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            accessbean.getEmf().getCache().evict(UssdTransaction.class);
            Query q = em
                    .createNativeQuery("select * from ussd_transaction u where "
                            + "u.incoming_message like '" + searchString + "%' or u.msisdn like '" + searchString + "%'"
                            + "GROUP BY u.incoming_message;", UssdTransaction.class);
            q.setHint("javax.persistence.cache.storeMode", "REFRESH");
            q.setParameter(1, searchString);
            q.setMaxResults(20);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<MenuItem> findMenus() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            TypedQuery<MenuItem> q = accessbean.createNamedQuery("MenuItem.findMenus", MenuItem.class);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public JsonObject dashboardData() {
        JsonObject obj = new JsonObject();
        obj.addProperty("users", findDistinctUsers().size());
        obj.addProperty("sessions", findDistinctTransactions().size());
        obj.addProperty("templates", findDistinctUSSDTemplates().size());
        obj.addProperty("codes", findMenus().size());
        return obj;
    }

//select count(sessionid) from ussd_transaction u where u.incoming_time between '2019-02-11 00:00:00' and '2019-02-11 23:59:59';
//select count(distinct sessionid)  from ussd_transaction u where u.incoming_time between '2019-02-11 00:00:00' and '2019-02-11 23:59:59';
//select count(distinct msisdn)  from ussd_transaction u where u.incoming_time between '2019-02-11 00:00:00' and '2019-02-11 23:59:59';
//select network, count(distinct sessionid) as 'count'  from ussd_transaction u where u.incoming_time between '2019-02-11 00:00:00' and '2019-02-11 23:59:59' group by network;
//select network, count(distinct msisdn) as 'count' from ussd_transaction u where u.incoming_time between '2019-02-11 00:00:00' and '2019-02-11 23:59:59' group by network;
    public Integer getFullSessionInteractionCount(Date startDate, Date endDate) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select count(sessionid) from ussd_transaction u where u.incoming_time between ?1 and ?2");
            Query query = em.createNativeQuery(sb.toString());
            query.setParameter(1, startDate);
            query.setParameter(2, endDate);
            List listResult = query.getResultList();
            Long x = (Long) listResult.get(0);
            return x.intValue();
        } finally {
            em.close();
        }
    }

    public Integer getDistinctSessionCount(Date startDate, Date endDate) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select count(distinct sessionid) from ussd_transaction u where u.incoming_time between ?1 and ?2");
            Query query = em.createNativeQuery(sb.toString());
            query.setParameter(1, startDate);
            query.setParameter(2, endDate);
            List listResult = query.getResultList();
            Long x = (Long) listResult.get(0);
            return x.intValue();
        } finally {
            em.close();
        }
    }

    public Integer getDistinctMsisdnCount(Date startDate, Date endDate) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select count(distinct msisdn) from ussd_transaction u where u.incoming_time between ?1 and ?2");
            Query query = em.createNativeQuery(sb.toString());
            query.setParameter(1, startDate);
            query.setParameter(2, endDate);
            List listResult = query.getResultList();
            Long x = (Long) listResult.get(0);
            return x.intValue();
        } finally {
            em.close();
        }
    }

    public List<GenericCount> getNetworkBySessionCount(Date startDate, Date endDate) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select network as 'name', count(distinct sessionid) as 'count'  from ussd_transaction u where u.incoming_time between ?1 and ?2 group by network");
            Query query = em.createNativeQuery(sb.toString());
            query.setParameter(1, startDate);
            query.setParameter(2, endDate);
            return (List<GenericCount>) query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<GenericCount> getNetworkByMsisdnCount(Date startDate, Date endDate) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select network as 'name', count(distinct msisdn) as 'count' from ussd_transaction u where u.incoming_time between ?1 and ?2 group by network");
            Query query = em.createNativeQuery(sb.toString());
            query.setParameter(1, startDate);
            query.setParameter(2, endDate);
            return (List<GenericCount>) query.getResultList();
        } finally {
            em.close();
        }
    }

    public HashMap<String, Object> dashboardData(Date startDate, Date endDate) {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("fullsession", getFullSessionInteractionCount(startDate, endDate));
        obj.put("distinctsession", getDistinctSessionCount(startDate, endDate));
        obj.put("distinctmsisdn", getDistinctMsisdnCount(startDate, endDate));
        obj.put("networkBySession", getNetworkBySessionCount(startDate, endDate));
        obj.put("networkByMsisdn", getNetworkByMsisdnCount(startDate, endDate));
        return obj;
    }

    public UssdTransactionLog createTxnLog(UssdTransactionLog log) {
        accessbean.create(log);
        return log;
    }

}
