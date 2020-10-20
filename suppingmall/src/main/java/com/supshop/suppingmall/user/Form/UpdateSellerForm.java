package com.supshop.suppingmall.user.Form;


import com.supshop.suppingmall.user.StoreVO;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class UpdateSellerForm {

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$")
    private String password;

    @NotEmpty
    private String storePrivateNumber;
    @NotEmpty
    private String storeName;
    @NotEmpty
    @Pattern(regexp = "^(((0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]|70))-(\\d{3,4}))|(\\d{4}))-(\\d{4})$")
    private String storePhoneNumber;

    @Pattern(regexp = "^\\d{6}$")
    private String storeZipCode;
    @NotEmpty
    private String storeAddress;
    @NotEmpty
    private String storeAddressDetail;
    private String storeApplyYn;
    private LocalDateTime storeRegisteredDate;

}
