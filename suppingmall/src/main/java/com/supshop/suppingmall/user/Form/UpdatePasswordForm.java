package com.supshop.suppingmall.user.Form;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class UpdatePasswordForm {

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$")
    String password;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$")
    String newPassword;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$")
    String newPasswordCheck;

}
