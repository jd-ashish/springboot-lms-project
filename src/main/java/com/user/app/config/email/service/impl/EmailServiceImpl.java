package com.user.app.config.email.service.impl;

import com.user.app.config.email.EmailDetails;
import com.user.app.config.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    @Override
    public String sendSimpleMail(EmailDetails details) {
        StringBuilder sb = new StringBuilder();
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            sb.append("Email sent successfully!");
        }
        return sb.toString();
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) {

        try{
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMailMessage,true);
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            //atachement
            FileSystemResource file
                    = new FileSystemResource(
                    new File(details.getAttachment()));

            mailMessage.addAttachment(
                    file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMailMessage);
            return "Mail sent Successfully";
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }
}
