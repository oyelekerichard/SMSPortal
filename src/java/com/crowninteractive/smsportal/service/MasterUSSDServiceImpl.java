/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.service;

import com.crowninteractive.smsportal.dto.BaseResponse;
import com.crowninteractive.smsportal.dto.Generic;
import com.crowninteractive.smsportal.enums.MessageReturnType;
import com.crowninteractive.smsportal.model.MenuItem;
import com.crowninteractive.smsportal.model.Settings;
import com.crowninteractive.smsportal.model.ShortCode;
import com.crowninteractive.smsportal.model.USSDTemplate;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import com.crowninteractive.smsportal.util.ResponseCodes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author Oluremi Adekanmbi
 * <oluremi.adekanmbi@etranzact.com>
 */
public class MasterUSSDServiceImpl {

    private final DBAccessBean accessbean;
    private static final Logger L = Logger.getLogger(MasterUSSDServiceImpl.class);
    private final static MasterUSSDServiceImpl INSTANCE = new MasterUSSDServiceImpl();
    private final static UssdTxnServiceImpl UINSTANCE = UssdTxnServiceImpl.getInstance();
    private final static AccountManagementService AINSTANCE = AccountManagementService.getInstance();
    private final String lineSeperator = "~";

    public DBAccessBean getAccessbean() {
        return accessbean;
    }

    public static MasterUSSDServiceImpl getInstance() {
        return INSTANCE;
    }

    private MasterUSSDServiceImpl() {
        accessbean = new DBAccessBean("SMSPortalPU");
    }

    public void createMenu(MenuItem menuItem) {
        accessbean.create(menuItem);
    }

    public MenuItem findMenu(Long id) {
        return accessbean.findSingle(MenuItem.class, id);
    }

    public ShortCode findShortCode(Long id) {
        return accessbean.findSingle(ShortCode.class, id);
    }

    public ShortCode findShortCode(String incoming) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        ShortCode sc = null;
        String query = "select s from ShortCode s where s.shortCode =:shortcode";
        try {
            return (ShortCode) accessbean.findSingleWithSingleParameters(query, "shortcode", incoming);
        } catch (NoResultException e) {
            return sc;
        }
    }

    public MenuItem findMenuWithIncoming(String incoming) {
        MenuItem mi = null;
        try {
            String query = "select m from MenuItem m where m.shortCode =:shortcode";
            return (MenuItem) accessbean.findSingleWithSingleParameters(query, "shortcode", incoming);
        } catch (NoResultException e) {
            return mi;
        }
    }

    private String getSearchKey(String dialedShortcode, String position) {
        String[] split = dialedShortcode.split("\\*");
        StringBuilder sb = new StringBuilder("*");
        sb.append(split[1]).append("*");
        sb.append(split[2]).append("_");
        sb.append(position);
        L.info("Search Key " + sb.toString());
        return sb.toString();
    }

    private String getSearchKeyDefault(String dialedShortcode) {
        String[] split = dialedShortcode.split("\\*");
        StringBuilder sb = new StringBuilder("*");
        sb.append(split[1]).append("*");
        sb.append(split[2]).append("_");
        sb.append("DEFAULT");
        return sb.toString();
    }

    public static String getShortCodeStyle(String shortCode) {
        String substring = shortCode.substring(0, shortCode.length() - 1);
        String[] split = substring.split("\\*");
        Settings setting = AINSTANCE.findSetting("EXEMPTED");
        String exempted = setting.getCurrentValue();
        //String exempted = "5433";
        StringBuilder sb = new StringBuilder("*");
        for (int i = 0; i < split.length; i++) {
            String string = split[i];

            switch (i) {
                case 0:
                    break;
                case 1:
                    sb.append(string).append("*");
                    break;
                default:
                    int passedVal;
                    try {
                        passedVal = Integer.parseInt(string);
                        if (exempted.contains(string)) {
                            sb.append(string).append("*");
                        } else if (passedVal > 50) {
                            sb.append("ANY").append("*");
                        } else {
                            sb.append(string).append("*");
                        }
                    } catch (NumberFormatException numberFormatException) {
                        sb.append("ANY").append("*");
                    }
                    break;
            }
        }
        String finalString = sb.toString().substring(0, sb.toString().length() - 1);
        return finalString + "#";
    }

    public static void printList(List<String> queue) {
        int counter = 0;
        for (String string : queue) {
            System.out.println(counter + ". " + string);
            counter++;
        }
    }

    public List<USSDTemplate> getTemplates() {
        return accessbean.findAll(USSDTemplate.class);
    }

    public USSDTemplate createTemplate(USSDTemplate template) {
        return accessbean.merge(template);
    }

    public USSDTemplate findTemplateById(long id) {
        return accessbean.findSingle(USSDTemplate.class, id);
    }

    public MenuItem findMenuItemById(long id) {
        return accessbean.findSingle(MenuItem.class, id);
    }

    public BaseResponse createShortCode(Generic generic) {
        BaseResponse resp;
        try {
            MenuItem incoming = findMenuWithIncoming(generic.getCode());
            if (incoming != null) {
                return new BaseResponse(ResponseCodes.CODE_ALREADY_EXIST, ResponseCodes.getDefaultMessageFor(ResponseCodes.CODE_ALREADY_EXIST));
            }

            USSDTemplate template = findTemplateById(generic.getTemplateId());
            String merchantName = generic.getMerchantName();
            String replaceAll = merchantName.toUpperCase().replaceAll(" ", "_");

            if (template.getTemplateName().equalsIgnoreCase("CHURCH")) {
                MenuItem m = new MenuItem();
                m.setReturnType(MessageReturnType.STATIC);
                m.setCreated(DateTimeUtil.getCurrentDate());
                m.setFinalMenu(false);
                m.setMakeServiceCallBefore(true);
                m.setMenuItem(new MenuItem(2l));
                m.setShortCode(generic.getCode());
                m.setServiceName(template.getTemplateClass());
                m.setMerchantIdentifier(replaceAll);
                MenuItem mi = new MenuItem();
                mi.setReturnType(MessageReturnType.APPLICATION);
                mi.setCreated(DateTimeUtil.getCurrentDate());
                mi.setFinalMenu(false);
                mi.setMakeServiceCallBefore(true);
                mi.setMenuItem(new MenuItem(8l));
                mi.setShortCode(generic.getCode().replaceAll("#", "*0#"));
                mi.setServiceName(template.getTemplateClass());
                mi.setServiceSection("ADDACCOUNT");
                mi.setMerchantIdentifier(replaceAll);
                accessbean.create(mi);
                StringBuilder sb = new StringBuilder();
                sb.append(generic.getWelcome()).append(lineSeperator);
                int menuCounter = 0;
                if (generic.getOne() != null && !generic.getOne().equals("")) {
                    menuCounter += 1;
                    sb.append("1. ").append(generic.getOne()).append(lineSeperator);
                }
                if (generic.getTwo() != null && !generic.getTwo().equals("")) {
                    menuCounter += 1;
                    sb.append("2. ").append(generic.getTwo()).append(lineSeperator);
                }
                if (generic.getThree() != null && !generic.getThree().equals("")) {
                    menuCounter += 1;
                    sb.append("3. ").append(generic.getThree()).append(lineSeperator);
                }
                if (generic.getFour() != null && !generic.getFour().equals("")) {
                    menuCounter += 1;
                    sb.append("4. ").append(generic.getFour()).append(lineSeperator);
                }
                if (generic.getFive() != null && !generic.getFive().equals("")) {
                    menuCounter += 1;
                    sb.append("5. ").append(generic.getFive()).append(lineSeperator);
                }
                if (generic.getSix() != null && !generic.getSix().equals("")) {
                    menuCounter += 1;
                    sb.append("6. ").append(generic.getSix()).append(lineSeperator);
                }
                if (generic.getSeven() != null && !generic.getSeven().equals("")) {
                    menuCounter += 1;
                    sb.append("7. ").append(generic.getSeven()).append(lineSeperator);
                }
                m.setMenuMessage(sb.toString());
                m.setNumberOfMenus(menuCounter);
                accessbean.create(m);
                MenuItem mi2 = new MenuItem();
                mi2.setReturnType(MessageReturnType.STATIC);
                mi2.setCreated(DateTimeUtil.getCurrentDate());
                mi2.setFinalMenu(false);
                mi2.setMenuMessage(sb.toString());
                mi2.setMakeServiceCallBefore(true);
                mi2.setMenuItem(new MenuItem(16l));
                mi2.setShortCode(generic.getCode().replaceAll("#", "*ANY#"));
                mi2.setServiceName(template.getTemplateClass());
                mi2.setMerchantIdentifier(replaceAll);
                accessbean.create(mi2);
            } else if (template.getTemplateName().equalsIgnoreCase("DATA") || template.getTemplateName().equalsIgnoreCase("QRIOS_DATA")) {
                MenuItem m = new MenuItem();
                m.setReturnType(MessageReturnType.STATIC);
                m.setCreated(DateTimeUtil.getCurrentDate());
                m.setFinalMenu(false);
                m.setMakeServiceCallBefore(true);
                m.setMenuItem(new MenuItem(36l));
                m.setShortCode(generic.getCode());
                m.setServiceName(template.getTemplateClass());
                m.setMerchantIdentifier(replaceAll);
                StringBuilder sb = new StringBuilder();
                sb.append(generic.getWelcome()).append(lineSeperator);
                int menuCounter = 0;
                if (generic.getOne() != null && !generic.getOne().equals("")) {
                    menuCounter += 1;
                    sb.append("1. ").append(generic.getOne()).append(lineSeperator);
                }
                if (generic.getTwo() != null && !generic.getTwo().equals("")) {
                    menuCounter += 1;
                    sb.append("2. ").append(generic.getTwo()).append(lineSeperator);
                }
                if (generic.getThree() != null && !generic.getThree().equals("")) {
                    menuCounter += 1;
                    sb.append("3. ").append(generic.getThree()).append(lineSeperator);
                }
                if (generic.getFour() != null && !generic.getFour().equals("")) {
                    menuCounter += 1;
                    sb.append("4. ").append(generic.getFour()).append(lineSeperator);
                }
                if (generic.getFive() != null && !generic.getFive().equals("")) {
                    menuCounter += 1;
                    sb.append("5. ").append(generic.getFive()).append(lineSeperator);
                }
                if (generic.getSix() != null && !generic.getSix().equals("")) {
                    menuCounter += 1;
                    sb.append("6. ").append(generic.getSix()).append(lineSeperator);
                }
                if (generic.getSeven() != null && !generic.getSeven().equals("")) {
                    menuCounter += 1;
                    sb.append("7. ").append(generic.getSeven()).append(lineSeperator);
                }
                m.setMenuMessage(sb.toString());
                m.setNumberOfMenus(menuCounter);
                accessbean.create(m);
            } else {
                L.info("Starting menu item section......");
                try {
                    MenuItem m = new MenuItem();
                    m.setReturnType(MessageReturnType.APPLICATION);
                    m.setCreated(DateTimeUtil.getCurrentDate());
                    m.setFinalMenu(false);
                    m.setMakeServiceCallBefore(true);
                    MenuItem menuItem = UINSTANCE.findMenuItem(template.getNextMenuId());
                    m.setMenuItem(menuItem);
                    m.setShortCode(generic.getCode());
                    m.setServiceName(template.getTemplateClass());
                    m.setServiceSection(template.getStartSection());
                    m.setMerchantIdentifier(replaceAll);
                    m.setMenuMessage("");
                    m.setNumberOfMenus(1);
                    accessbean.create(m);
                } catch (Exception e) {
                    L.info("Exception....");
                    e.printStackTrace();
                }
                L.info("Done with menu item section......");
            }

            resp = new BaseResponse();
        } catch (Exception e) {
            resp = new BaseResponse(ResponseCodes.INTERNAL_SYSTEM_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.INTERNAL_SYSTEM_ERROR));
        }
        return resp;
    }

    public BaseResponse editShortCode(Generic generic) {
        BaseResponse resp;
        try {
            USSDTemplate template = findTemplateById(generic.getTemplateId());
            String merchantName = generic.getMerchantName();
            String replaceAll = merchantName.toUpperCase().replaceAll(" ", "_");
            if (template.getTemplateName().equals("CHURCH")
                    || template.getTemplateName().equals("DATA")
                    || template.getTemplateName().equals("QRIOS_DATA")) {
                MenuItem m = findMenuWithIncoming(generic.getCode());

                StringBuilder sb = new StringBuilder();
                sb.append(generic.getWelcome()).append(lineSeperator);
                int menuCounter = 0;
                if (generic.getOne() != null && !generic.getOne().equals("")) {
                    menuCounter += 1;
                    sb.append("1. ").append(generic.getOne()).append(lineSeperator);
                }
                if (generic.getTwo() != null && !generic.getTwo().equals("")) {
                    menuCounter += 1;
                    sb.append("2. ").append(generic.getTwo()).append(lineSeperator);
                }
                if (generic.getThree() != null && !generic.getThree().equals("")) {
                    menuCounter += 1;
                    sb.append("3. ").append(generic.getThree()).append(lineSeperator);
                }
                if (generic.getFour() != null && !generic.getFour().equals("")) {
                    menuCounter += 1;
                    sb.append("4. ").append(generic.getFour()).append(lineSeperator);
                }
                if (generic.getFive() != null && !generic.getFive().equals("")) {
                    menuCounter += 1;
                    sb.append("5. ").append(generic.getFive()).append(lineSeperator);
                }
                if (generic.getSix() != null && !generic.getSix().equals("")) {
                    menuCounter += 1;
                    sb.append("6. ").append(generic.getSix()).append(lineSeperator);
                }
                if (generic.getSeven() != null && !generic.getSeven().equals("")) {
                    menuCounter += 1;
                    sb.append("7. ").append(generic.getSeven()).append(lineSeperator);
                }
                m.setMenuMessage(sb.toString());
                m.setNumberOfMenus(menuCounter);
                m.setModified(DateTimeUtil.getCurrentDate());
                accessbean.merge(m);

                if (template.getTemplateName().equals("CHURCH")) {
                    m = findMenuWithIncoming(generic.getCode().toUpperCase().replaceAll("#", "*ANY#"));
                    m.setMenuMessage(sb.toString());
                    m.setModified(DateTimeUtil.getCurrentDate());
                    m.setNumberOfMenus(0);
                    accessbean.merge(m);
                }

            } else {
                MenuItem m = new MenuItem();
                m.setReturnType(MessageReturnType.APPLICATION);
                m.setCreated(DateTimeUtil.getCurrentDate());
                m.setFinalMenu(false);
                m.setMakeServiceCallBefore(true);
                m.setMenuItem(new MenuItem(template.getNextMenuId()));
                m.setShortCode(generic.getCode());
                m.setServiceName(template.getTemplateClass());
                m.setServiceSection(template.getStartSection());
                m.setMerchantIdentifier(replaceAll);
                m.setMenuMessage("");
                m.setModified(DateTimeUtil.getCurrentDate());
                m.setNumberOfMenus(1);
                accessbean.create(m);
            }
            resp = new BaseResponse();
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponse(ResponseCodes.INTERNAL_SYSTEM_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.INTERNAL_SYSTEM_ERROR));
        }
        return resp;
    }

    public List<MenuItem> getMenuItems() {
        TypedQuery<MenuItem> createQuery = null;
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = "select m from MenuItem m where m.shortCode IS NOT NULL";
            createQuery = em.createQuery(sql, MenuItem.class);
            return createQuery.getResultList();
        } finally {
            em.close();
        }

    }

    public Generic findShortCodeDetails(long id) {
        Generic g = new Generic();
        MenuItem m = findMenuItemById(id);
        g.setCode(m.getShortCode());
        g.setMerchantName(m.getMerchantIdentifier());
        String[] splitted = m.getMenuMessage().split("~");
        String[] split;
        for (int i = 0; i < splitted.length; i++) {
            String string = splitted[i];
            split = string.split("\\.");
            switch (i) {
                case 0:
                    g.setWelcome(string);
                    break;
                case 1:
                    g.setOne(split[1].trim());
                    break;
                case 2:
                    g.setTwo(split[1].trim());
                    break;
                case 3:
                    g.setThree(split[1].trim());
                    break;
                case 4:
                    g.setFour(split[1].trim());
                    break;
                case 5:
                    g.setFive(split[1].trim());
                    break;
                case 6:
                    g.setSix(split[1].trim());
                    break;
                case 7:
                    g.setSeven(split[1].trim());
                    break;
            }
        }

        return g;
    }

    public BaseResponse deleteCode(String code) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            code = code.replaceAll("#", "");
            String query = "SELECT m FROM MenuItem m WHERE m.shortCode LIKE CONCAT('%',:code,'%')";
            TypedQuery<MenuItem> createQuery = accessbean.createQuery(query, MenuItem.class);
            createQuery.setParameter("code", code);
            List<MenuItem> menuItems = createQuery.getResultList();
            for (MenuItem menuItem : menuItems) {
                accessbean.delete(menuItem);
            }
            return new BaseResponse();
        } catch (Exception e) {
            return new BaseResponse(ResponseCodes.INTERNAL_SYSTEM_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.INTERNAL_SYSTEM_ERROR));
        } finally {
            em.close();
        }
    }
}
