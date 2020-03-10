package com.supshop.suppingmall.payment;

import com.supshop.suppingmall.mapper.PaymentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private PaymentMapper paymentMapper;

    public PaymentService(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    public Long insertPayment(Payment payment) {
        paymentMapper.insertPayment(payment);
        return payment.getPaymentId();
    }

    public Payment retrievePayment(Long id) {
        return paymentMapper.findOne(id);
    }

    public List<Payment> retrievePayments() {
        return paymentMapper.findAll();
    }
}
