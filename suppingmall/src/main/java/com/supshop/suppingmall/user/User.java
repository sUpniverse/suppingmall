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
    private String nikcName;
    private String address;
    private String addreDetail;
    private String postNo;
    private String phoneNumber;
    private LocalDateTime createDate;
    private LocalDateTime updateData;
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
        Admin("000"),
        Seller("001"),
        User("002");

        private String code;

        public static Role getCodeString(String code) {
            return Arrays.stream(Role.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }
    }
}
