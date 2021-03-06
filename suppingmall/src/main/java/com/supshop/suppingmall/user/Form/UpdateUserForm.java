package com.supshop.suppingmall.user.Form;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class UpdateUserForm {

    private String email;
    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$")
    private String password;
    private String name;
    private String nickName;
    private String address;
    private String addressDetail;
    private String zipCode;
    private String phoneNumber;

}
