/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.email;

import core.data.Email;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Diana
 */
public class SendEmail {
    
    public Email email;

    public SendEmail() {
        email = new Email();
    }
    public SendEmail(Email email) {
        this.email = email;
    }
    
    public void SendMail() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email.getUsername(), email.getPassWord());
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getUsername()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email.getTo()));
            message.setSubject(email.getSubject());
            message.setText(email.getMensage());

            Transport.send(message);
            System.out.println("Correo enviado");
            
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);            
        }
    }
    
    
}
