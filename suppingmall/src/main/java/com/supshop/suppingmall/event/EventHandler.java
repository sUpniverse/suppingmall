package com.supshop.suppingmall.event;

import com.supshop.suppingmall.mail.CustomMailSender;
import com.supshop.suppingmall.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;


@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final CustomMailSender customMailSender;
    private final SpringTemplateEngine templateEngine;
    private final String subject = "님, suppingmall 가입 인증 메일입니다.";


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = UserCreatedEvent.class)
    public void handle(UserCreatedEvent event) {
        log.debug("메일 발송 이벤트 수신");

        User user = event.getUser();

        Context context = new Context();
        context.setVariable("token", user.getUserConfirmation().getConfirmToken());
        context.setVariable("name", user.getNickName());
        String html = templateEngine.process("mail/confirm", context);

        try {
            customMailSender.sendEmail(user.getEmail(),user.getNickName()+subject,html);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
