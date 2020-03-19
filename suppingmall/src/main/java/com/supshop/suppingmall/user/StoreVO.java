package com.supshop.suppingmall.user;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class StoreVO {

    private final String number = "^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]))-(\\d{3,4})-(\\d{4})$";

    private String storePrivateNumber;
    @NotEmpty
    private String storeName;

    @Pattern(regexp = number)
    private String storePhoneNumber;
    @NotEmpty
    private String storeZipCode;
    @NotEmpty
    private String storeAddress;
    private String storeAddressDetail;
    private String storeApplyYn;
}