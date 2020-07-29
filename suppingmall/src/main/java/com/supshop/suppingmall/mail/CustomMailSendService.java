package com.supshop.suppingmall.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class CustomMailSendService {

    private final JavaMailSender sender;
    private MimeMessage message;
    private MimeMessageHelper messageHelper;

    public void sendEmail(Mail mail) throws MessagingException {
        message = sender.createMimeMessage();
        messageHelper = new MimeMessageHelper(message,"UTF-8");
        setEmail(mail);
        sender.send(message);
    }

    private void setEmail(Mail mail) throws MessagingException {
        messageHelper.setTo(mail.getTo());
        messageHelper.setSubject(mail.getSubject());
        messageHelper.setText(mail.getText(),true);
    }
}
