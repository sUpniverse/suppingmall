package com.supshop.suppingmall.delivery;

import com.supshop.suppingmall.user.User;
import org.springframework.stereotype.Component;

@Component
public class DeliveryFactory {

    public Delivery buildDelivery(User user) {
        return Delivery.builder()
                .name(user.getName())
                .address(user.getAddress())
                .address(user.getAddressDetail())
                .zipCode(user.getZipCode())
                .phone(user.getPhoneNumber())
                .vendor(Delivery.DeliveryVendor.CJ)
                .build();
    }

}
