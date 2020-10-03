package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.payment.Payment;

import java.util.List;

public interface PaymentMapper {

    void save(List<Payment> payment);

    Payment findOne(Long id);

    List<Payment> findAll();

    void updateStatus(Payment payment);
}
