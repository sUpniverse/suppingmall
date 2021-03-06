package com.supshop.suppingmall.event;

import com.supshop.suppingmall.mail.CustomMailSendService;
import com.supshop.suppingmall.mail.Mail;
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

    private final CustomMailSendService customMailSendService;
    private final SpringTemplateEngine templateEngine;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = UserEvent.class)
    public void handle(UserEvent event) {
        log.debug("메일 발송 이벤트 수신");

        Mail mail;

        if(event.getEventType().equals(EventType.CREATED))
            mail = userCreatedEventHandler(event);
        else
            mail = userUpdatedEventHandler(event);

        try {
            customMailSendService.sendEmail(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private Mail userCreatedEventHandler(UserEvent event) {
        User user = event.getUser();

        String subject = "님, suppingmall 가입 인증 메일입니다.";

        Context context = new Context();
        context.setVariable("token", user.getUserConfirmation().getConfirmToken());
        context.setVariable("name", user.getNickName());
        String html = templateEngine.process("mail/confirm", context);

        Mail mail = Mail.builder()
                .to(user.getEmail())
                .subject(user.getNickName() + subject)
                .text(html)
                .build();

        return mail;
    }

    private Mail userUpdatedEventHandler(UserEvent event) {
        User user = event.getUser();

        String subject = "님, suppingmall 비밀번호 변경 메일입니다.";

        Context context = new Context();
        context.setVariable("password", user.getPassword());
        context.setVariable("name", user.getNickName());
        String html = templateEngine.process("mail/password", context);

        Mail mail = Mail.builder()
                .to(user.getEmail())
                .subject(user.getNickName() + subject)
                .text(html)
                .build();

        return mail;
    }
}
