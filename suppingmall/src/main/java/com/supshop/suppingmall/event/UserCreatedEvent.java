package com.supshop.suppingmall.event;

import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class UserCreatedEvent {

    private User user;
    private LocalDateTime publishedTime;

}
