package com.supshop.suppingmall.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {
    MASTER("운영자","U000"),
    ADMIN("관리자","U001"),
    SELLER("판매회원","U002"),
    USER("일반회원","U003");

    private String title;
    private String code;

    public static Role getCodeString(String code) {
        return Arrays.stream(Role.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
    }
}
