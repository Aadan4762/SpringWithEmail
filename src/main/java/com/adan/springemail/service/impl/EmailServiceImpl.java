package com.adan.springemail.service.impl;

import com.adan.springemail.service.EmailService;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("aliadesh631@gmail.com")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public String sendMail(MultipartFile[] file, String to, String[] cc, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);


            if (cc != null && cc.length > 0) {
                mimeMessageHelper.setCc(cc);
            }

            //mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);

            if (file != null && file.length > 0) {
                for (int i = 0; i < file.length; i++) {
                    mimeMessageHelper.addAttachment(
                            file[i].getOriginalFilename(),
                            new ByteArrayResource(file[i].getBytes()));
                }
            }

            javaMailSender.send(mimeMessage);

            return "Mail sent successfully";

        } catch (Exception e) {
          //  throw new RuntimeException(e);
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }

    }

    @Override
    public void monitorIncomingEmails() {
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");

            String emailProvider = "imap.gmail.com";
            String username = "aliadesh631@gmail.com";
            String password = "kdwqvsityiiigcmq";

            store.connect(emailProvider, username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.getMessages();

            for (Message message : messages) {
                String subject = message.getSubject();
                Address[] fromAddresses = message.getFrom();

                System.out.println("Subject: " + subject);
                System.out.println("From: " + fromAddresses[0]);

                if (message instanceof MimeMessage) {
                    MimeMessage mimeMessage = (MimeMessage) message;
                    Object content = mimeMessage.getContent();
                    if (content instanceof String) {
                        System.out.println("Content: " + (String) content);
                    }
                }

                message.setFlag(Flags.Flag.SEEN, true);
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


