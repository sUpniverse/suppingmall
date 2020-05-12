package com.supshop.suppingmall.user.Form;

import com.supshop.suppingmall.user.Role;
import com.supshop.suppingmall.user.StoreVO;
import com.supshop.suppingmall.user.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;


@Getter @Setter
@AllArgsConstructor
@ToString @Builder
public class SignUpForm {

    private Long userId;

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9_]+[A-Za-z0-9]*[@]{1}[A-Za-z0-9]+[A-Za-z0-9]*[.]{1}[A-Za-z]{1,3}$")
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$")
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
    @Pattern(regexp = "^(?:(010\\d{4})|(01[1|6|7|8|9]\\d{3,4}))(\\d{4})$")
    private String phoneNumber;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private String delYn;
    private Role role;
    private User.LoginType type;

    private StoreVO storeVO;
}
