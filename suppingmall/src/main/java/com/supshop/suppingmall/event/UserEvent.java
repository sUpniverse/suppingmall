package com.supshop.suppingmall.event;

import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
public class UserEvent extends Event{

    private User user;

    public UserEvent(EventType eventType, LocalDateTime publishedTime, User user) {
        super(eventType, publishedTime);
        this.user = user;
    }
}
