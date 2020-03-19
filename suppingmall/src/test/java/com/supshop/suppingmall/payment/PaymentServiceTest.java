package com.supshop.suppingmall.payment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Test
    @Transactional
    public void insertTest() throws Exception {
        //given
        Payment payment = Payment.builder()
                .price(200000)
                .paymentType(Payment.PayGroupType.CARD)
                .status(Payment.PaymentStatus.COMPLETE)
                .vendorCheckNumber("001-222-3333")
                .build();
        //when
        Long payId = paymentService.save(payment);
        Payment paymentTest = paymentService.findPayment(payId);

        //then
        assertEquals(paymentTest.getPrice(),payment.getPrice());
        assertEquals(paymentTest.getPaymentType(),payment.getPaymentType());
        assertEquals(paymentTest.getStatus(),payment.getStatus());
        assertEquals(paymentTest.getVendorCheckNumber(),payment.getVendorCheckNumber());
    }

}