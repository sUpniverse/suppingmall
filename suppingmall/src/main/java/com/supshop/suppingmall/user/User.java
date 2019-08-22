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

    int userId;
    String email;
    String password;
    String name;
    String nikcName;
    String address;
    String addreDetail;
    String postNo;
    String phoneNumber;
    LocalDateTime createDate;
    LocalDateTime updateData;
    String delYn;
    Role role;
    String shopName;
    String shopNumber;
    String shopAddress;
    String shopAddressDetail;
    String shopPostNo;

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
