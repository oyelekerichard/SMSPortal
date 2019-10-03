/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.service;

import com.crowninteractive.smsportal.dto.BaseResponse;
import com.crowninteractive.smsportal.dto.Notification;
import com.crowninteractive.smsportal.dto.ResponseObj;
import com.crowninteractive.smsportal.enums.UserState;
import com.crowninteractive.smsportal.model.NavigationItem;
import com.crowninteractive.smsportal.model.Permission;
import com.crowninteractive.smsportal.model.Role;
import com.crowninteractive.smsportal.model.Settings;
import com.crowninteractive.smsportal.model.User;
import com.crowninteractive.smsportal.model.dto.GenericModel;
import com.crowninteractive.smsportal.notification.MailerHelper;
import com.crowninteractive.smsportal.util.DateTimeUtil;
import com.crowninteractive.smsportal.util.ResponseCodes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author CROWN-STAFF
 */
public class AccountManagementService {

    private final DBAccessBean accessbean;
    private static AccountManagementService INSTANCE = new AccountManagementService();
    private final Logger L = Logger.getLogger(AccountManagementService.class);

    public static AccountManagementService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AccountManagementService();
        }
        return INSTANCE;
    }

    private AccountManagementService() {
        accessbean = new DBAccessBean("SMSPortalPU");
    }

    public List<User> findUsers(int page, int size) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = String.format("select * from sms_user h order by h.id desc;");
            Query query = em.createNativeQuery(sql, User.class);
            List<User> users = (List<User>) query.getResultList();
            return users;
        } finally {
            em.close();
        }

        //return accessbean.findAll(User.class, page, size);
    }

    public List<Role> findRoles() {
        return accessbean.findAll(Role.class);
    }

    public List<GenericModel> findRolesV2() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String query = "select "
                    + "id,"
                    + "permission,"
                    + "navigation,"
                    + "name,"
                    + "description,"
                    + "created_date,"
                    + "modified_date,"
                    + "(select CONCAT_WS(' ', u.first_name, u.last_name) from sms_user u where u.id = h.created_by) as 'created_by',"
                    + "(select CONCAT_WS(' ', u.first_name, u.last_name) from sms_user u where u.id = h.modified_by) as 'modified_by'"
                    + "from sms_role h;";
            Query nativeQuery = accessbean.createNativeQuery(query, GenericModel.class);
            return (List<GenericModel>) nativeQuery.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Permission> findPermissions() {
        return accessbean.findAll(Permission.class);
    }

    public List<NavigationItem> findNavigations() {
        return accessbean.findAll(NavigationItem.class);
    }

    public Role createRole(Role role, List<Permission> permissions, User user) {
        String ps[] = new String[permissions.size()];
        int counter = 0;
        for (Permission permission : permissions) {
            if (Boolean.valueOf(permission.getDescription())) {
                ps[counter] = permission.getId().toString();
                counter++;
            }
        }
        role.setPermission(ps);
        role.setCreatedBy(user.getId());
        role.setCreated(DateTimeUtil.getCurrentDate());
        accessbean.create(role);
        return role;
    }

    public Role createRole(Role role, List<Permission> permissions, List<NavigationItem> navigations, User user) {
        String ps[] = new String[permissions.size()];
        int counter = 0;
        for (Permission permission : permissions) {
            if (Boolean.valueOf(permission.getDescription())) {
                ps[counter] = permission.getId().toString();
                counter++;
            }
        }
        role.setPermission(ps);
        String p[] = new String[navigations.size()];
        counter = 0;
        for (NavigationItem navigation : navigations) {
            if (Boolean.valueOf(navigation.getDescription())) {
                p[counter] = navigation.getId().toString();
                counter++;
            }
        }
        role.setNavigation(p);
        role.setCreatedBy(user.getId());
        role.setCreated(DateTimeUtil.getCurrentDate());
        accessbean.create(role);
        return role;
    }

    public Role updateRole(Role role, List<Permission> permissions, Long id, User user) {
        Role r = accessbean.findSingle(Role.class, id);
        String ps[] = new String[permissions.size()];
        int counter = 0;
        for (Permission permission : permissions) {
            if (Boolean.valueOf(permission.getDescription())) {
                ps[counter] = permission.getId().toString();
                counter++;
            }
        }
        r.setName(role.getName());
        r.setDescription(role.getDescription());
        r.setPermission(ps);
        r.setModifiedBy(user.getId());
        r.setModified(DateTimeUtil.getCurrentDate());
        accessbean.merge(r);
        return role;
    }

    public Role updateRole(Role role, List<Permission> permissions, List<NavigationItem> navigations, Long id, User user) {
        Role r = accessbean.findSingle(Role.class, id);
        String ps[] = new String[permissions.size()];
        int counter = 0;
        for (Permission permission : permissions) {
            if (Boolean.valueOf(permission.getDescription())) {
                ps[counter] = permission.getId().toString();
                counter++;
            }
        }
        r.setPermission(ps);
        String p[] = new String[navigations.size()];
        counter = 0;
        for (NavigationItem navigation : navigations) {
            if (Boolean.valueOf(navigation.getDescription())) {
                p[counter] = navigation.getId().toString();
                counter++;
            }
        }
        r.setNavigation(p);
        r.setName(role.getName());
        r.setDescription(role.getDescription());
        r.setModifiedBy(user.getId());
        r.setModified(DateTimeUtil.getCurrentDate());
        r = accessbean.merge(r);
        L.info(r);
        return role;
    }

    public User findUser(Long id) {
        return (User) accessbean.findSingle(User.class, id);
    }

    public Role findUserRole(Long userId) {
        User user = findUser(userId);
        return user.getRole();
    }

    public BaseResponse createUser(User user) {
        try {
            L.info(user);
            if (!userExist(user.getEmail())) {
                user.setCreationDate(DateTimeUtil.getCurrentDate());
                user.setUserState(UserState.NEW_USER);
                int size = findAllUsers().size();
                user.setAccountNumber(StringUtils.leftPad((size + 1) + "", 5, "0"));
                user.setPassword("password@1");
                accessbean.merge(user);

                Notification notif = new Notification();
                notif.setFullName(user.getLastName() + " " + user.getFirstName());
                notif.setLink("http://81.26.64.41:28080/smsportal/login.jsp");
                notif.setUserEmail(user.getEmail());
                notif.setPassword(user.getPassword());
                notif.setProvider("Crown Interactive");
                notif.setDateOfTransaction(DateTimeUtil.toPrettyString(new Date()));
                Map map = MailerHelper.generateReceiptMap(notif);
                String htmlString = MailerHelper.getHTMLString(map, MailerHelper.getFTLName("ACCOUNT_CREATION"));
                //Mailer.sendSSLMessage(user.getEmail(), "CrownInteractive SMS Portal Account Creation", htmlString, "Crown Interactive");
                MailerHelper.sendEmail(notif, "CrownInteractive SMS Portal Account Creation", "ACCOUNT_CREATION", user.getEmail());
            } else {
                return new BaseResponse(ResponseCodes.ACCOUNT_ALREADY_EXISTS, ResponseCodes.getDefaultMessageFor(ResponseCodes.ACCOUNT_ALREADY_EXISTS));
            }
            return new BaseResponse();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse(ResponseCodes.INTERNAL_SYSTEM_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.INTERNAL_SYSTEM_ERROR));
        }
    }

    public User updateUser(User user) {
        User merge = accessbean.merge(user);
        L.info(merge);
        return merge;
    }

    public List<User> findAllUsers() {
        return accessbean.findAll(User.class);
    }

    public List<User> search(String search) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String query = "SELECT u from User u "
                    + "where u.lastName LIKE CONCAT('%',:search,'%') "
                    + "OR u.firstName LIKE CONCAT('%',:search,'%') "
                    + "OR u.email LIKE CONCAT('%',:search,'%')";
            TypedQuery<User> q = accessbean.createQuery(query, User.class);
            q.setParameter("search", search);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Role findRoleById(Long id) {
//        String sql = String.format("select * from sms_role where id = " + id + ";");
//        Query query = accessbean.getEm().createNativeQuery(sql, Role.class);

        return accessbean.findSingle(Role.class, id);
    }

    public User login(String username, String password) {
        User mergedUser = null;
        String query = "SELECT u FROM User u where u.email =:email";
        Query createQuery = accessbean.createQuery(query, User.class);
        createQuery.setParameter("email", username);
        try {
            User user = (User) createQuery.getSingleResult();
            String incoming = password;
            if (incoming.equals(user.getPassword())) {
                if (!(user.getUserState() == UserState.NEW_USER)) {
                    user.setLastLoginDate(user.getCurrentLoginDate());
                    user.setCurrentLoginDate(DateTimeUtil.getCurrentDate());
                    user.setUserState(UserState.LOGGED_IN);
                    mergedUser = accessbean.merge(user);
                } else {
                    mergedUser = user;
                }
            }
            return mergedUser;
        } catch (Exception e) {
            e.printStackTrace();
            return mergedUser;
        }
    }

    public String changeDefaultPassword(Long userId, String oldPassword, String newPassword) {
        String resp = null;

        User user = findUser(userId);
        //String incoming = Crypto.digestPassword(oldPassword);
        String incoming = oldPassword;
        if (user.getPassword().equals(incoming)) {
            //user.setPassword(Crypto.digestPassword(newPassword));
            user.setLastLoginDate(user.getCurrentLoginDate());
            user.setCurrentLoginDate(DateTimeUtil.getCurrentDate());
            user.setUserState(UserState.LOGGED_IN);
            user.setPassword(newPassword);
            accessbean.merge(user);
            resp = "Success";
        } else {
            resp = "Password could not be changed";
        }

        return resp;
    }

    public String changePassword(Long userId, String oldPassword, String newPassword) {
        String resp;
        User user = findUser(userId);
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            accessbean.merge(user);
            resp = "Password successfully changed. Logout to use your new password!";
        } else {
            resp = "Password could not be changed";
        }
        return resp;
    }

    public Boolean userExist(String email) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            boolean retVal;
            String query = "SELECT u FROM User u WHERE u.email = :email";
            List<User> users = accessbean.findAllWithSingleParameters(User.class, query, "email", email);
            if (users.size() > 0) {
                retVal = true;
            } else {
                retVal = false;
            }

            return retVal;
        } finally {
            em.close();
        }
    }

    public User changeUserState(Long userId, UserState userState) {
        User user = accessbean.findSingle(User.class, userId);
        if (null == user.getUserState()) {
            user.setUserState(UserState.DEACTIVATED);
        } else {
            switch (user.getUserState()) {
                case DEACTIVATED:
                    user.setUserState(UserState.REACTIVATED);
                    break;
                case NEW_USER:
                    break;
                default:
                    user.setUserState(UserState.DEACTIVATED);
                    break;
            }
        }
        User merged = accessbean.merge(user);
        return merged;
    }

    public String logout(User u) {
        User user = accessbean.findSingle(User.class, u.getId());
        user.setUserState(UserState.LOGGED_OUT);
        User merged = accessbean.merge(user);
        return merged.getEmail() + " successfully logged out!";
    }

    public Integer getUsersCount() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = String.format("select count(*) from sms_user;");
            Query query = em.createNativeQuery(sql);
            List listResult = query.getResultList();
            Long x = (Long) listResult.get(0);
            return x.intValue();
        } finally {
            em.close();
        }
    }

    public Integer getRolesCount() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = String.format("select count(*) from sms_role;");
            Query query = em.createNativeQuery(sql);
            List listResult = query.getResultList();
            Long x = (Long) listResult.get(0);
            return x.intValue();
        } finally {
            em.close();
        }
    }

    public Integer getChannelsCount() {
//        String sql = String.format("select count(*) from hms_channel;");
//        Query query = accessbean.getEm().createNativeQuery(sql);
//        List listResult = query.getResultList();
//        Long x = (Long) listResult.get(0);
        return 0;
    }

    public Integer getPermissionsCount() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String sql = String.format("select count(*) from sms_permission_list;");
            Query query = em.createNativeQuery(sql); //createNativeQuery(sql);
            List listResult = query.getResultList();
            Long x = (Long) listResult.get(0);
            return x.intValue();
        } finally {
            em.close();
        }
    }

    public ResponseObj getUserManagementStats(int page, int size) {
        ResponseObj obj = new ResponseObj();
        List<User> users = findUsers(page, size);
        obj.setStart(page == 0 ? 1 : page);
        obj.setSize(users.size());
        obj.setUsers(getUsersCount());
        obj.setChannels(getChannelsCount());
        obj.setRoles(getRolesCount());
        obj.setPermissions(getPermissionsCount());
        obj.setPermissiondata(findPermissions());
        obj.setUserdata(users);
        obj.setRoledata(findRoles());
        return obj;
    }

    public BaseResponse updateUserRole(Long userId, Long roleId) {
        User user = findUser(userId);
        if (user.getId() != null) {
            Role role = findRoleById(roleId);
            if (role.getId() != null) {
                user.setRole(role);
                accessbean.merge(user);
            } else {
                return new BaseResponse(ResponseCodes.PERSISTENCE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.PERSISTENCE_ERROR));
            }
        } else {
            return new BaseResponse(ResponseCodes.PERSISTENCE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.PERSISTENCE_ERROR));
        }
        return new BaseResponse();
    }

    public Settings findSetting(String identifier) {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String query = "select s from Settings s where s.identifier =:identifier";
            TypedQuery<Settings> q = em.createQuery(query, Settings.class);
            q.setParameter("identifier", identifier);
            return q.getSingleResult();
        } catch (Exception e) {
            return findSetting();
        } finally {
            em.close();
        }
    }

    private Settings findSetting() {
        EntityManager em = accessbean.getEmf().createEntityManager();
        try {
            String query = "select s from Settings s where s.identifier =:identifier";
            TypedQuery<Settings> q = em.createQuery(query, Settings.class);
            q.setParameter("identifier", "SEND_SMS");
            return q.getSingleResult();
        } catch (Exception e) {
            return findSetting("SEND_SMS");
        } finally {
            em.close();
        }
    }

    public Settings createSetting(Settings setting) {
        setting.setCreateDate(DateTimeUtil.getCurrentDate());
        setting.setModifiedDate(DateTimeUtil.getCurrentDate());
        accessbean.create(setting);
        return setting;
    }

    public BaseResponse changeSetting(Long id, String newValue) {
        Settings settings = accessbean.findSingle(Settings.class, id);
        if (settings == null) {
            return new BaseResponse(ResponseCodes.EMPTY_RECORDS, ResponseCodes.getDefaultMessageFor(ResponseCodes.EMPTY_RECORDS));
        }

        settings.setLastValue(settings.getCurrentValue());
        settings.setCurrentValue(newValue);
        settings.setModifiedDate(DateTimeUtil.getCurrentDate());
        Settings merge = accessbean.merge(settings);
        return new BaseResponse(merge);
    }

    public BaseResponse deleteSetting(Long id) {
        Settings settings = accessbean.findSingle(Settings.class, id);
        if (settings == null) {
            return new BaseResponse(ResponseCodes.EMPTY_RECORDS, ResponseCodes.getDefaultMessageFor(ResponseCodes.EMPTY_RECORDS));
        }
        accessbean.delete(settings);
        return new BaseResponse();
    }

    public HashMap<String, Object> findAllSettings(int page, int size) {
        HashMap<String, Object> map = new HashMap<>();
        List<Settings> settings = accessbean.findAll(Settings.class, page, size);
        map.put("settings", settings);
        Integer count = accessbean.findCount(Settings.class);
        String query = "select s from settings s;";
        accessbean.createNativeQuery(query, Settings.class);
        map.put("total", count);
        map.put("size", count < size ? count : size);
        map.put("page", page);
        return map;
    }

    public List<String> getSetting(String identifier) {
        List<String> retVal = new ArrayList<>();
        Settings setting = findSetting(identifier);
        String currentValue = setting.getCurrentValue();
        String[] split = currentValue.split(",");
        for (String string : split) {
            retVal.add(string.trim());
        }
        return retVal;
    }

}
