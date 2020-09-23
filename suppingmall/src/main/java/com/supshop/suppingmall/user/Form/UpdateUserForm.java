package com.supshop.suppingmall.user.Form;


import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class UpdateUserForm {

    private String email;
    private String password;
    private String name;
    private String nickName;
    private String address;
    private String addressDetail;
    private String zipCode;
    private String phoneNumber;

}
