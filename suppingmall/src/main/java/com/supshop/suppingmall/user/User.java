package com.supshop.suppingmall.user;

import lombok.*;

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
    private String city;
    private String cityDetail;
    private String zipCode;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private String delYn;
    private Role role;
    private LoginType type;

    private String shopName;
    private String shopNumber;
    private String shopAddress;
    private String shopAddressDetail;
    private String shopPostNo;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = "010" + phoneNumber;
    }

    @Getter
    @AllArgsConstructor
    public enum Role {
        ADMIN("U000"),
        SELLER("U001"),
        USER("U002");

        private String code;


        public static Role getCodeString(String code) {
            return Arrays.stream(Role.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum LoginType {
        LOCAL("000"),
        GOOGLE("001"),
        KAKAO("002");

        private String code;


        public static LoginType getCodeString(String code) {
            return Arrays.stream(LoginType.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }
    }
}
