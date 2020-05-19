package com.supshop.suppingmall.common;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

public class MailService {

    private JavaMailSender sender;
    private MimeMessage message;
    private MimeMessageHelper messageHelper;

}
