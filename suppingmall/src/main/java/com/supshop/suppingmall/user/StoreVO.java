package com.supshop.suppingmall.user;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class StoreVO {

    private String storePrivateNumber;
    private String storeName;
    private String storePhoneNumber;
    private String storeZipCode;
    private String storeAddress;
    private String storeAddressDetail;
    private String storeApplyYn;
}
