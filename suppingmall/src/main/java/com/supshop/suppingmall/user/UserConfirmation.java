package com.supshop.suppingmall.user;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@ToString @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserConfirmation {

    private Long userId;
    private Long confirmId;
    private String confirmToken;
    private LocalDateTime sendDate;
    private LocalDateTime confirmDate;

}
