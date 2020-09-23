package com.supshop.suppingmall.user.Form;


import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class UpdatePasswordForm {

    String password;
    String newPassword;
    String newPasswordCheck;

}
