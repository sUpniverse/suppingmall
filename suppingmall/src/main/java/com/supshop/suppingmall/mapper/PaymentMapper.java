package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.payment.Payment;

import java.util.List;

public interface PaymentMapper {

    void insertPayment(Payment payment);

    Payment findOne(Long id);

    List<Payment> findAll();
}
