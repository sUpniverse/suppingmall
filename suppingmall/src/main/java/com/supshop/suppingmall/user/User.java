package com.supshop.suppingmall.user;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    private int userId;
    private String email;
    private String password;
    private String name;
    private String nickName;
    private String city;
    private String downtown;
    private String address;
    private String addressDetail;
    private String postNo;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private String delYn;
    private Role role;
    private String shopName;
    private String shopNumber;
    private String shopAddress;
    private String shopAddressDetail;
    private String shopPostNo;

    @Getter
    @AllArgsConstructor
    public enum Role {
        Admin("U000"),
        Seller("U001"),
        User("U002");

        private String code;

        public static Role getCodeString(String code) {
            return Arrays.stream(Role.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }
    }
}
