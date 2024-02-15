package com.sampledashboard1.service.serviceImpl;

import com.sampledashboard1.payload.request.MailRequest;
import com.sampledashboard1.repository.AppConstantRepository;
import com.sampledashboard1.service.EmailService;
import com.sampledashboard1.utils.VariableUtils;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import jakarta.mail.*;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private String FROM_EMAIL;
    private String FROM_NAME;
    private String HOST;
    private String PORT;
    private String SMTP_PASSWORD;
    private String SMTP_USERNAME;

    private final AppConstantRepository appConstantRepository;
    @Override
    @Transactional
    public String sendEmail(MailRequest request) {
        Properties props = getPropertiesForSES();
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });
        session.setDebug(true);
        String res = "";

        try {
            // Create a message with the specified information.
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(request.getTo()));
            msg.setSubject(request.getSubject());
            msg.setText(request.getBody());

            Transport transport = session.getTransport();
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

            log.info("{} -> Email sent :: {} ", request.getTo(), request.getSubject());
            res = "Success";

            return Boolean.TRUE.toString();
        }catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public Properties getPropertiesForSES() {
        setProperties();
        Properties props = System.getProperties();
        props.put("mail.smtp.user", SMTP_USERNAME);
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "true");
//        props.put("mail.smtp.ssl.checkserveridentity", true);
        return props;
    }
    private void setProperties() {
        FROM_EMAIL = appConstantRepository.getValueByTitle(VariableUtils.FROM_EMAIL).orElse("no-reply@techhive.co.in");
        FROM_NAME = appConstantRepository.getValueByTitle(VariableUtils.FROM_NAME).orElse("techHive");
        HOST = appConstantRepository.getValueByTitle(VariableUtils.HOST).orElse("mail.techhive.co.in");
        PORT = appConstantRepository.getValueByTitle(VariableUtils.PORT).orElse("465");
        SMTP_PASSWORD = appConstantRepository.getValueByTitle(VariableUtils.SMTP_PASSWORD).orElse("^bul561>4e1v");
        SMTP_USERNAME = appConstantRepository.getValueByTitle(VariableUtils.SMTP_USERNAME).orElse("common@techhive.co.in");


       /* FROM_EMAIL = "no-reply@techhive.co.in";
        FROM_NAME = "techHive";
        HOST = "mail.techhive.co.in";
        PORT = "465" ;
        SMTP_PASSWORD = "^bul561>4e1v";
        SMTP_USERNAME = "common@techhive.co.in";*/

    }

}
