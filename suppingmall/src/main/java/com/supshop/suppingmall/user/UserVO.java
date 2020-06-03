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
public class UserVO {

    //password가 없는 객체 session에서 사용하기 위함
    private Long userId;
    private String email;
    private String name;
    private String nickName;
    private String address;
    private String addressDetail;
    private String zipCode;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private Role role;
    private User.LoginType type;
    private StoreVO storeVO;
    
}
