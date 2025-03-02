package com.app.bankX.service;

import com.app.bankX.dto.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String senderEmail;


    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {

        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMessageBody());

            javaMailSender.send(mailMessage);

            System.out.println("Email sent successfully");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendEmailWithAttachment(EmailDetails emailDetails) {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper;
            try{
                mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                mimeMessageHelper.setFrom(senderEmail);
                mimeMessageHelper.setTo(emailDetails.getRecipient());
                mimeMessageHelper.setText(emailDetails.getMessageBody());
                mimeMessageHelper.setSubject(emailDetails.getSubject());

                FileSystemResource file = new FileSystemResource(emailDetails.getAttachment());
                mimeMessageHelper.addAttachment(file.getFilename(), file);

                javaMailSender.send(mimeMessage);

                log.info(file.getFilename()+" has been sent.");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }


    }
}
