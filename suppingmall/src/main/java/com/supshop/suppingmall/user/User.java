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

    private final String emailReg = "^[A-Za-z0-9_]+[A-Za-z0-9]*[@]{1}[A-Za-z0-9]+[A-Za-z0-9]*[.]{1}[A-Za-z]{1,3}$";
    private final String passwordReg = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$";
    private final String phoneReg = "(01[016789])([0-9]{3,4})([0-9]{4})$";

    private Long userId;

    @NotEmpty
    @Pattern(regexp = emailReg)
    private String email;

    @NotEmpty
    @Pattern(regexp = passwordReg)
    private String password;

    @NotEmpty
    private String name;

    @NotEmpty
    private String nickName;

    @NotEmpty
    private String address;
    private String addressDetail;

    @NotEmpty
    private String zipCode;

    @NotEmpty
    @Pattern(regexp = phoneReg)
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


    @Getter
    @AllArgsConstructor
    public enum Role {
        MASTER("U000"),
        ADMIN("U001"),
        SELLER("U002"),
        USER("U003");

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
