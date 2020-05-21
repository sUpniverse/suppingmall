package com.supshop.suppingmall.user;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Arrays;

@Getter @Setter
@ToString @Builder
@EqualsAndHashCode(of = "userId")
@NoArgsConstructor @AllArgsConstructor
public class User {

    private Long userId;
    private String email;
    private String password;
    private String name;
    private String nickName;
    private String address;
    private String addressDetail;
    private String zipCode;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private String delYn;
    private String emailConfirmYn;
    private Role role;
    private LoginType type;
    private UserConfirmation userConfirmation;
    private StoreVO storeVO;


    @Getter
    @AllArgsConstructor
    public enum LoginType {
        LOCAL("일반로그인","000","email"),
        GOOGLE("구글","001","sub"),
        FACEBOOK("페이스북","002","facebook"),
        NAVER("네이버","003","naver"),
        KAKAO("카카오","004","kakao");

        private String title;
        private String code;
        private String key;

        public static LoginType getCodeString(String code) {
            return Arrays.stream(LoginType.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }
    }
}
