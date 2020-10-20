package com.supshop.suppingmall.user.Form;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class ApplySellerForm {

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


}
