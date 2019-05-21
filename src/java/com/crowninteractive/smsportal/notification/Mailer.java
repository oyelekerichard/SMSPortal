/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.notification;

import com.sun.net.ssl.internal.ssl.Provider;
import java.security.Security;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;

public class Mailer {

    private static final Logger L = Logger.getLogger(Mailer.class);
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    String[] recipients;
    String subject;
    String message;
    String from;

    public static void main(String[] args)
            throws Exception {
        Security.addProvider(new Provider());
        String email;
        email = "adekanmbi.oluremi@gmail.com";
        Mailer.sendSSLMessage(email, "hi", "TEXT", "paymentalerts43@gmail.com");
        System.out.println("Sucessfully Sent mail to All Users");
    }

    public Mailer() {
        Security.addProvider(new Provider());
    }

    public Mailer(String username, String password) {
        Security.addProvider(new Provider());
    }

    public static boolean sendSSLMessage(String recipient, String subject, String message, String from)
            throws MessagingException, Exception {
        L.info("*#* Sending SSL Message now!!!!!");
//         props.put("mail.smtp.host", "smtp.gmail.com");
        try {
            Properties props = new Properties();
//         props.put("mail.smtp.auth", "true");
//         props.put("mail.smtp.starttls.enable", "true");
//         props.put("mail.smtp.port", "465");
//         props.put("mail.smtp.user", "paymentalerts43@gmail.com");
//         props.put("mail.smtp.password", "@Paymentalerts43");
//         props.put("mail.smtp.socketFactory.port", "465");
//         props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//         props.put("mail.smtp.socketFactory.fallback", "false");

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.user", "paymentalerts43@gmail.com");
            props.put("mail.smtp.password", "@Paymentalerts43");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("paymentalerts43@gmail.com", "@Paymentalerts43");
                }
            });
//         props.put("mail.smtp.auth", "true");
//         props.put("mail.smtp.starttls.enable", "true");
//         props.put("mail.smtp.host", "mail.porplecom.net");
//         props.put("mail.smtp.port", "465");
//         props.put("mail.smtp.user", "info@porplecom.net");
//         props.put("mail.smtp.password", "Olaseni1");
//         props.put("mail.smtp.socketFactory.port", "465");
//         props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//         props.put("mail.smtp.socketFactory.fallback", "false");
//         Session session = Session.getInstance(props, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//               return new PasswordAuthentication("info@porplecom.net", "Olaseni1");
//            }
//         });
//
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);
            Message m = new MimeMessage(session);
            m.setFrom(new InternetAddress(from));
            m.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            m.setSubject(subject);
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, "text/html");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String logoURL = "C:\\Users\\adekanmbi\\Desktop\\Desktop Files\\smsportal.png";
            //String logoURL = "/usr/share/tomcat/porple.png";
            DataSource fds = new FileDataSource(logoURL);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);
            m.setContent(multipart);
            Transport.send(m);
            L.info("*#* Sent message successfully....");
            return true;
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
            return false;
        }
    }
}
