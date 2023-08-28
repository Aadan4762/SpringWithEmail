package com.adan.springemail.service.impl;

import com.adan.springemail.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

          /**  for (int i = 0; i < file.length; i++) {
                mimeMessageHelper.addAttachment(
                        file[i].getOriginalFilename(),
                        new ByteArrayResource(file[i].getBytes()));
            }
           **/
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

}
