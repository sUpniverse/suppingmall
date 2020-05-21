package com.supshop.suppingmall.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class CustomMailSender {

    private final JavaMailSender sender;
    private MimeMessage message;
    private MimeMessageHelper messageHelper;

    public void sendEmail(String email, String subject, String text) throws MessagingException {
        message = sender.createMimeMessage();
        messageHelper = new MimeMessageHelper(message,"UTF-8");
        setEmail(email, subject, text);
        sender.send(message);
    }

    private void setEmail(String email,String subject,String text) throws MessagingException {
        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(text,true);
    }
}
