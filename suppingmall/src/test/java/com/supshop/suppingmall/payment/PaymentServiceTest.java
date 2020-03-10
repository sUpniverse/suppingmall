package com.supshop.suppingmall.payment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Test
    @Transactional
    public void insertTest() throws Exception {
        //given
        PayInfo payInfo = PayInfo.builder().amount(200000).name("맥북프로").build();
        //when
        Long payId = paymentService.insertPayment(null);
        Payment paymentTest = paymentService.retrievePayment(payId);

        //then
//        assertThat(paymentTest.getPrice()).isEqualTo(payment.getPrice());
//        assertThat(paymentTest.getPaymentType()).isEqualTo(payment.getPaymentType());
//        assertThat(paymentTest.getVendor().getTitle()).isEqualTo(payment.getVendor().getTitle());
//        assertThat(paymentTest.getInstallmentMonth()).isEqualTo(payment.getInstallmentMonth());
//        assertThat(paymentTest.getStatus()).isEqualTo(payment.getStatus());

    }

}