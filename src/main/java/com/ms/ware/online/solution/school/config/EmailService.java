package com.ms.ware.online.solution.school.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailService {

    String message = "Sent";
    @Getter
    boolean status;
    @Autowired
    private DB db;
    private String sender = null;
    private String password = null;


    private Session session = null;

    public void init() {
        try {
            for (Map<String, Object> list : db.getMapRecord("SELECT email,password,`host`,port FROM sender_email ORDER BY RAND() LIMIT 1")) {
                sender = list.get("email").toString();
                password = list.get("password").toString();
                String host = list.get("host").toString();
                String port = list.get("port").toString();
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, password);
                    }
                });
                break;
            }
        } catch (Exception ignored) {
        }
    }

    public boolean sendmail(String receiver, String subject, String body) {
        try {
            javax.mail.Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sender, false));
            msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(receiver));
            msg.setSubject(subject);
            msg.setContent(body, "text/html");
            msg.setSentDate(new Date());
            Transport.send(msg);
            status = true;
            System.out.println(body + "Email sent from " + sender);
        } catch (MessagingException e) {
            System.out.println("Email Sending error " + e.getMessage());
            message = e.getMessage();
            status = false;
        }
        return status;
    }

    public String getMsg() {
        return message;
    }
}
