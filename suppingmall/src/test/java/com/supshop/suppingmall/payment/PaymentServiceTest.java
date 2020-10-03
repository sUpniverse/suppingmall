package com.supshop.suppingmall.payment;

import com.supshop.suppingmall.order.OrderFactory;
import com.supshop.suppingmall.order.OrderItem;
import com.supshop.suppingmall.order.Orders;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentServiceTest {

    @Autowired PaymentService paymentService;
    @Autowired OrderFactory orderFactory;

    @Test
    @Transactional
    public void insertTest() throws Exception {
        int size = paymentService.findPayments().size();
        Orders orders = orderFactory.createTempOrder();
        List<OrderItem> orderItems = orders.getOrderItems();
        //given
        List<Payment> paymentList = new ArrayList<>();
        Payment payment = Payment.builder()
                .price(200000)
                .paymentType(Payment.PayGroupType.CARD)
                .status(Payment.PaymentStatus.COMPLETE)
                .vendorCheckNumber("001-222-3333")
                .orderItem(orderItems.get(0))
                .build();

        paymentList.add(payment);

        Payment payment2 = Payment.builder()
                .price(150000)
                .paymentType(Payment.PayGroupType.CARD)
                .status(Payment.PaymentStatus.COMPLETE)
                .vendorCheckNumber("001-222-3355")
                .orderItem(orderItems.get(1))
                .build();

        paymentList.add(payment2);
        //when
        paymentService.save(paymentList);
        int afterInsertSize = paymentService.findPayments().size();

        //then
        Assert.assertEquals(size+2, afterInsertSize);
    }

}