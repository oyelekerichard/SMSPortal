/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.controller;

import com.crowninteractive.smsportal.dto.BaseResponse;
import com.crowninteractive.smsportal.dto.PermissionNavigation;
import com.crowninteractive.smsportal.dto.ResponseObj;
import com.crowninteractive.smsportal.model.NavigationItem;
import com.crowninteractive.smsportal.model.Permission;
import com.crowninteractive.smsportal.model.Role;
import com.crowninteractive.smsportal.model.Settings;
import com.crowninteractive.smsportal.model.User;
import com.crowninteractive.smsportal.model.dto.GenericModel;
import com.crowninteractive.smsportal.service.AccountManagementService;
import com.crowninteractive.smsportal.util.ResponseCodes;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Adekanmbi Oluremi adekanmbi.oluremi@gmail.com 08098753155
 */
@Path("/user/")
public class UserController {

    private final AccountManagementService USERSERVICE = AccountManagementService.getInstance();
    private final Logger L = Logger.getLogger(UserController.class);

    @Context
    private HttpServletRequest servletRequest;
    @Context
    private HttpServletResponse servletResponse;

    @GET
    @Path("/test")
    @Produces({"application/json"})
    public BaseResponse test() {
        return new BaseResponse();
    }

    @GET
    @Path(value = "findUsers/{page}/{size}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse findUsers(
            @PathParam("page") int page,
            @PathParam("size") int size) {
        ResponseObj userManagementStats = USERSERVICE.getUserManagementStats(page, size);
        return new BaseResponse(userManagementStats);
    }

    @POST
    @Path(value = "addUser/{roleId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse addUser(@PathParam("roleId") Long roleId,
            User user) {
        BaseResponse resp = null;
        try {
            HttpSession session = servletRequest.getSession(true);
            Role role = USERSERVICE.findRoleById(roleId);
            user.setRole(role);
            resp = USERSERVICE.createUser(user);
            resp = new BaseResponse();
        } catch (PersistenceException pe) {
            L.warn(pe.getMessage());
            resp = new BaseResponse(ResponseCodes.PERSISTENCE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.PERSISTENCE_ERROR));
        } catch (ConstraintViolationException cve) {
            L.warn(cve.getMessage());
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                sb.append(violation.getMessage());
            }
            resp = new BaseResponse(ResponseCodes.CONSTRAINT_VIOLATION_ERROR, sb.toString());
        } catch (Exception e) {
            L.warn(e.getMessage());
            resp = new BaseResponse(ResponseCodes.INTERNAL_SYSTEM_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.INTERNAL_SYSTEM_ERROR));
        }
        return resp;
    }

    @POST
    @Path(value = "create")
    public void createUser(@FormParam("lastName") String lastName,
            @FormParam("firstName") String firstName,
            @FormParam("phone") String phone,
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("rpassword") String rpassword) {
        BaseResponse resp = null;
        try {
            L.info(firstName + " : " + lastName + " : " + phone + " : " + email + " : " + password + " : " + rpassword);
            HttpSession session = servletRequest.getSession(true);
            L.info("Session created");
            if (password.equals(rpassword)) {
                L.info("Checked password matched");
                User user = new User();
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPassword(password);
                user.setPhoneNumber(phone);
                resp = USERSERVICE.createUser(user);
                if (resp.getRetn() == ResponseCodes.REQUEST_SUCCESSFUL) {
                    session.setAttribute("message", "Your account has been sucessfully created. Please login with your details.");
                } else {
                    session.setAttribute("message", resp.getDesc());
                }
            } else {
                session.setAttribute("message", "Passwords do not match!");
            }
            resp = new BaseResponse();
        } catch (PersistenceException pe) {
            L.warn(pe.getMessage());
            resp = new BaseResponse(ResponseCodes.PERSISTENCE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.PERSISTENCE_ERROR));
        } catch (ConstraintViolationException cve) {
            L.warn(cve.getMessage());
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                sb.append(violation.getMessage());
            }
            resp = new BaseResponse(ResponseCodes.CONSTRAINT_VIOLATION_ERROR, sb.toString());
        } catch (Exception e) {
            L.warn(e.getMessage());
            resp = new BaseResponse(ResponseCodes.INTERNAL_SYSTEM_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.INTERNAL_SYSTEM_ERROR));
        }
    }

    @POST
    @Path(value = "login")
    public void login(
            @FormParam("username") String username,
            @FormParam("password") String password) {
        L.info(username);
        User user = null;
        try {
            user = USERSERVICE.login(username, password);
            HttpSession session = servletRequest.getSession(true);
            if (user != null) {
                switch (user.getUserState()) {
                    case DEACTIVATED:
                        session.setAttribute("message", "Deactivated Account! Please contact Administrator for more information.");
                        servletResponse.sendRedirect("/smsportal/login.jsp");
                        break;
                    case DELETED:
                        session.setAttribute("message", "Invalid Account! Please contact Administrator for more information.");
                        servletResponse.sendRedirect("/smsportal/login.jsp");
                        break;
                    case LOGGED_IN:
                        session.setAttribute("user", user);
                        servletResponse.sendRedirect("/smsportal/index.jsp");
                        break;
                    case NEW_USER:
                        session.setAttribute("user", user);
                        servletResponse.sendRedirect("/smsportal/changepassword.jsp");
                        break;
                    case REACTIVATED:
                        //Redirect to the Change Password page
                        session.setAttribute("user", user);
                        servletResponse.sendRedirect("/smsportal/index.jsp");
                        break;
                }
            } else {
                session.setAttribute("message", "Invalid Account! Username or Password incorrect!");
                servletResponse.sendRedirect("/smsportal/login.jsp");
            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            HttpSession session = servletRequest.getSession(true);
            session.setAttribute("message", "Invalid Account! Username or Password incorrect!");
            try {
                servletResponse.sendRedirect("/smsportal/login.jsp");
            } catch (IOException ex1) {
            }
        } catch (IOException ex) {
        }
        //return user;
    }

    @GET
    @Path(value = "/getCurrentUser")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse getCurrentUser() {
        BaseResponse resp = null;
        try {
            HttpSession session = servletRequest.getSession(true);
            User user = (User) session.getAttribute("user");
            if (user == null) {
                servletResponse.sendRedirect("/smsportal/login.jsp");
            }
            return new BaseResponse(user);
        } catch (PersistenceException pe) {
            L.warn(pe.getMessage());
            return new BaseResponse(ResponseCodes.PERSISTENCE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.PERSISTENCE_ERROR));
        } catch (ConstraintViolationException cve) {
            L.warn(cve.getMessage());
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                sb.append(violation.getMessage());
            }
            return new BaseResponse(ResponseCodes.CONSTRAINT_VIOLATION_ERROR, sb.toString());
        } catch (Exception e) {
            L.warn(e.getMessage());
            return new BaseResponse(ResponseCodes.INTERNAL_SYSTEM_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.INTERNAL_SYSTEM_ERROR));
        }
        //return resp;
    }

    @GET
    @Path(value = "/findRoles")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse findRoles() {
        BaseResponse resp = null;
        try {
            List<GenericModel> roles = USERSERVICE.findRolesV2();
            resp = new BaseResponse(roles);
        } catch (PersistenceException pe) {
            L.warn(pe.getMessage());
            resp = new BaseResponse(ResponseCodes.PERSISTENCE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.PERSISTENCE_ERROR));
        } catch (ConstraintViolationException cve) {
            L.warn(cve.getMessage());
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                sb.append(violation.getMessage());
            }
            resp = new BaseResponse(ResponseCodes.CONSTRAINT_VIOLATION_ERROR, sb.toString());
        } catch (Exception e) {
            L.warn(e.getMessage());
            resp = new BaseResponse(ResponseCodes.INTERNAL_SYSTEM_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.INTERNAL_SYSTEM_ERROR));
        }
        return resp;
    }

    @POST
    @Path(value = "createRole/{name}/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse createRole(@PathParam("name") String roleName,
            @PathParam("description") String description, List<Permission> permissions) {
        L.info(roleName);
        L.info(description);
        L.info(permissions);
        HttpSession session = servletRequest.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            try {
                servletResponse.sendRedirect("/smsportal/login.jsp");
            } catch (IOException ex) {

            }
        }
        Role createRole = USERSERVICE.createRole(new Role(roleName, description), permissions, user);
        if (createRole != null) {
            return new BaseResponse(createRole);
        } else {
            return new BaseResponse(ResponseCodes.OTHER_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.OTHER_ERROR));
        }
    }

    @POST
    @Path(value = "createRole2/{name}/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse createRole2(@PathParam("name") String roleName,
            @PathParam("description") String description, PermissionNavigation pn) {
        L.info(roleName);
        L.info(description);
        L.info(pn);
        HttpSession session = servletRequest.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            try {
                servletResponse.sendRedirect("/smsportal/login.jsp");
            } catch (IOException ex) {

            }
        }
        Role createRole = USERSERVICE.createRole(new Role(roleName, description), pn.getPermissions(), pn.getNavigations(), user);
        if (createRole != null) {
            return new BaseResponse(createRole);
        } else {
            return new BaseResponse(ResponseCodes.OTHER_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.OTHER_ERROR));
        }
    }

    @POST
    @Path(value = "updateRole/{name}/{description}/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse updateRole(@PathParam("name") String roleName,
            @PathParam("description") String description, @PathParam("id") Long id, List<Permission> permissions) {
        HttpSession session = servletRequest.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            try {
                servletResponse.sendRedirect("/smsportal/login.jsp");
            } catch (IOException ex) {

            }
        }
        Role createRole = USERSERVICE.updateRole(new Role(roleName, description), permissions, id, user);
        if (createRole != null) {
            return new BaseResponse(createRole);
        } else {
            return new BaseResponse(ResponseCodes.OTHER_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.OTHER_ERROR));
        }
    }

    @POST
    @Path(value = "updateRole2/{name}/{description}/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse updateRole2(@PathParam("name") String roleName,
            @PathParam("description") String description, @PathParam("id") Long id, PermissionNavigation pn) {
        HttpSession session = servletRequest.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            try {
                servletResponse.sendRedirect("/smsportal/login.jsp");
            } catch (IOException ex) {

            }
        }
        Role createRole = USERSERVICE.updateRole(new Role(roleName, description), pn.getPermissions(), pn.getNavigations(), id, user);
        if (createRole != null) {
            return new BaseResponse(createRole);
        } else {
            return new BaseResponse(ResponseCodes.OTHER_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.OTHER_ERROR));
        }
    }

    @GET
    @Path(value = "/logout")
    public void logout() {
        try {
            HttpSession session = servletRequest.getSession(true);
            session.getAttribute("user");
            servletResponse.sendRedirect("/smsportal/login.jsp");
        } catch (IOException ex) {

        }
    }

    @GET
    @Path(value = "updateUserRole/{userId}/{roleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse updateUserRole(@PathParam("userId") Long userId, @PathParam("roleId") Long roleId) {
        BaseResponse resp = null;
        try {
            resp = USERSERVICE.updateUserRole(userId, roleId);
        } catch (PersistenceException pe) {
            L.warn(pe.getMessage());
            resp = new BaseResponse(ResponseCodes.PERSISTENCE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.PERSISTENCE_ERROR));
        } catch (ConstraintViolationException cve) {
            L.warn(cve.getMessage());
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                sb.append(violation.getMessage());
            }
            resp = new BaseResponse(ResponseCodes.CONSTRAINT_VIOLATION_ERROR, sb.toString());
        } catch (Exception e) {
            L.warn(e.getMessage());
            resp = new BaseResponse(ResponseCodes.INTERNAL_SYSTEM_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.INTERNAL_SYSTEM_ERROR));
        }
        return resp;
    }

    @POST
    @Path(value = "/changeDefaultPassword")
    @Produces(MediaType.APPLICATION_JSON)
    public String changeDefaultPassword(
            @FormParam("oldpassword") String password,
            @FormParam("newpassword") String newpassword) {
        HttpSession session = servletRequest.getSession(true);
        User user = (User) session.getAttribute("user");
        Long userId = user.getId();
        String changePassword = USERSERVICE.changeDefaultPassword(userId, password, newpassword);
        try {
            if ("Success".equals(changePassword)) {
                servletResponse.sendRedirect("/smsportal/index.jsp");
            } else {
                servletResponse.sendRedirect("/smsportal/changepassword.jsp");
            }

        } catch (IOException ex) {

        }
        return changePassword;
    }

    @GET
    @Path(value = "/findPermissions")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse findPermissions() {
        BaseResponse resp = null;
        try {
            List<Permission> permissions = USERSERVICE.findPermissions();
            resp = new BaseResponse(permissions);
        } catch (PersistenceException pe) {
            L.warn(pe.getMessage());
            resp = new BaseResponse(ResponseCodes.PERSISTENCE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.PERSISTENCE_ERROR));
        } catch (ConstraintViolationException cve) {
            L.warn(cve.getMessage());
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                sb.append(violation.getMessage());
            }
            resp = new BaseResponse(ResponseCodes.CONSTRAINT_VIOLATION_ERROR, sb.toString());
        } catch (Exception e) {
            L.warn(e.getMessage());
            resp = new BaseResponse(ResponseCodes.INTERNAL_SYSTEM_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.INTERNAL_SYSTEM_ERROR));
        }
        return resp;
    }

    @GET
    @Path(value = "/findNavigations")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse findNavigations() {
        BaseResponse resp = null;
        try {
            List<NavigationItem> navigations = USERSERVICE.findNavigations();
            resp = new BaseResponse(navigations);
        } catch (PersistenceException pe) {
            L.warn(pe.getMessage());
            resp = new BaseResponse(ResponseCodes.PERSISTENCE_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.PERSISTENCE_ERROR));
        } catch (ConstraintViolationException cve) {
            L.warn(cve.getMessage());
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                sb.append(violation.getMessage());
            }
            resp = new BaseResponse(ResponseCodes.CONSTRAINT_VIOLATION_ERROR, sb.toString());
        } catch (Exception e) {
            L.warn(e.getMessage());
            resp = new BaseResponse(ResponseCodes.INTERNAL_SYSTEM_ERROR, ResponseCodes.getDefaultMessageFor(ResponseCodes.INTERNAL_SYSTEM_ERROR));
        }
        return resp;
    }

    @GET
    @Path("/findSettings/{page}/{size}")
    @Produces({"application/json"})
    public HashMap<String, Object> findSettings(
            @PathParam(value = "page") int page,
            @PathParam(value = "size") int size) {
        HashMap<String, Object> allSettings = USERSERVICE.findAllSettings(page, size);
        return allSettings;
    }

    @GET
    @Path("/changeSetting/{id}/{newValue}")
    @Produces({"application/json"})
    public BaseResponse changeSetting(
            @PathParam(value = "id") Long id,
            @PathParam(value = "newValue") String newValue) {
        BaseResponse changeSetting = USERSERVICE.changeSetting(id, newValue);
        return changeSetting;
    }

    @GET
    @Path("/deleteSetting/{id}")
    @Produces({"application/json"})
    public BaseResponse deleteSetting(
            @PathParam(value = "id") Long id) {
        BaseResponse deleteSetting = USERSERVICE.deleteSetting(id);
        return deleteSetting;
    }

    @POST
    @Path("/addSettings")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Settings addSetting(Settings settings) {
        Settings resp = USERSERVICE.createSetting(settings);
        return resp;
    }

}
