package com.supshop.suppingmall.payment;

import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@Component
@ActiveProfiles("test")
public class PaymentFactory {

    public Payment buildPaymentList(int price) {

        return Payment.builder()
                    .paymentType(Payment.PayGroupType.CARD)
                    .price(price)
                    .status(Payment.PaymentStatus.COMPLETE)
                    .payDate(LocalDateTime.now())
                    .vendorCheckNumber("vaf00")
                    .build();
    }
}
