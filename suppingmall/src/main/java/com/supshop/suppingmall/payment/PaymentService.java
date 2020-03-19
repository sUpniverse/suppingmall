package com.supshop.suppingmall.payment;

import com.supshop.suppingmall.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMapper paymentMapper;

    @Transactional
    public Long save(Payment payment) {
        paymentMapper.save(payment);
        return payment.getPaymentId();
    }

    public Payment findPayment(Long id) {
        return paymentMapper.findOne(id);
    }

    public List<Payment> findPayments() {
        return paymentMapper.findAll();
    }
}
