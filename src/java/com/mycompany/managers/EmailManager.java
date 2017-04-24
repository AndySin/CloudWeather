/*
 * Created by Tanya Sutan-Tanon on 2017.04.22  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.managers;

import javax.inject.Named;

import java.util.Properties;
import javax.enterprise.context.RequestScoped;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Tanya
 */
@Named(value = "emailManager")
@RequestScoped
public class EmailManager {

    // Create a new instance of EmailManager
    public EmailManager() {

        emailSubject = "Try Weather-Proof Activity Planner!";
    }

    /*
    ==================
    Instance Variables
    ==================
     */
    private final String DEFAULT_MESSAGE = "<p>Hey friend...</p><p>I thought I'd shoot "
            + "you this email to let you know about Weather-Proof Activity Planner. "
            + "Sign up for free and get access to: Personalized Activity Calendar, "
            + "Multiday Event Forecast, Real-Time Severe Weather Alerts, "
            + "Minute-by-Minute Forecast Accuracy, Email Reminder Notifications, "
            + "and Much More!</p>";
    private final String SITE_URL = "http://localhost:8080/CloudWeather/index.xhtml";

    private String emailTo;             // Contains only one email address to send email to
    private String emailSubject;        // Subject line of the email message
    private String emailBody;           // Email content created in HTML format with PrimeFaces Editor
    private String customizedMessage;          // Customized message to send.    
    Properties emailServerProperties;   // java.util.Properties
    Session emailSession;               // javax.mail.Session
    MimeMessage htmlEmailMessage;       // javax.mail.internet.MimeMessage

    private String statusMessage;

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getCustomizedMessage() {
        return customizedMessage;
    }

    public void setCustomizedMessage(String customizedMessage) {
        this.customizedMessage = customizedMessage;
    }

    public String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    /*
    ======================================================
    Create Email Sesion and Transport Email in HTML Format
    ======================================================
     */
    public void sendEmail(String fromEmail) throws AddressException, MessagingException {
        String[] emailToList = emailTo.split("\\s*,\\s*");

        emailBody = "<div align=\"center\"><p><b>YOU'VE BEEN INVITED TO <br> "
                + "ENJOY THE BEST WEATHER-BASED DECISION SUPPORT SYSTEM.</b></p>";
        if (customizedMessage != null && !customizedMessage.isEmpty()) {
            emailBody = emailBody + "<p>" + customizedMessage + "</p>";
        } else {
            emailBody += DEFAULT_MESSAGE;
        }
        emailBody = emailBody + "<form target=\"_blank\" action=\"" + SITE_URL + "\">"
                + "<input style=\"color: #fff; background-color: #4c4c4c; "
                + "text-shadow: -1px 1px #417cb8;  border: none;  padding: 15px 45px; "
                + "font-size: 20px; line-height: 1.8;\" type=\"submit\" "
                + "value=\"Try Weather-Proof Now!\"/></form>";
        emailBody = emailBody + "<br><small>You're being emailed because your friend "
                + "<a href=\"mailto:" + fromEmail + "\">"
                + fromEmail + "</a> has shared with you.</div>";

        // Set Email Server Properties
        emailServerProperties = System.getProperties();
        emailServerProperties.put("mail.smtp.port", "587");
        emailServerProperties.put("mail.smtp.auth", "true");
        emailServerProperties.put("mail.smtp.starttls.enable", "true");

        try {
            // Create an email session using the email server properties set above
            emailSession = Session.getDefaultInstance(emailServerProperties, null);

            /*
            Create a Multi-purpose Internet Mail Extensions (MIME) style email
            message from the MimeMessage class under the email session created.
             */
            htmlEmailMessage = new MimeMessage(emailSession);

            // Set the email TO field to emailTo for every email in the emailToList array
            for (String email : emailToList) {
                htmlEmailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }

            // It is okay for emailCc to be empty or null since the CC is optional
            // Set the email subject line
            htmlEmailMessage.setSubject(emailSubject);

            // Set the email body to the HTML type text
            htmlEmailMessage.setContent(emailBody, "text/html");

            // Create a transport object that implements the Simple Mail Transfer Protocol (SMTP)
            Transport transport = emailSession.getTransport("smtp");

            /*
            Connect to Gmail's SMTP server using the username and password provided.
            For the Gmail's SMTP server to accept the unsecure connection, the
            Cloud.Software.Email@gmail.com account's "Allow less secure apps" option is set to ON.
             */
            transport.connect("smtp.gmail.com", "weather.proof.activity.planner@gmail.com", "CSD@VaTech-1872");

            // Send the htmlEmailMessage created to the specified list of addresses (recipients)
            transport.sendMessage(htmlEmailMessage, htmlEmailMessage.getAllRecipients());

            // Close this service and terminate its connection
            transport.close();

            statusMessage = "Email is sent!";

        } catch (AddressException ae) {
            statusMessage = "Email Address Exception Occurred!";

        } catch (MessagingException me) {
            statusMessage = "Email Messaging Exception Occurred! Internet Connection Required!";
        }
    }

}
