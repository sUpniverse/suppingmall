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

    private String storePrivateNumber;
    private String storeName;
    private String storePhoneNumber;
    private String storeZipCode;
    private String storeAddress;
    private String storeAddressDetail;
    private String storeApplyYn;
    private LocalDateTime storeRegisteredDate;

}
