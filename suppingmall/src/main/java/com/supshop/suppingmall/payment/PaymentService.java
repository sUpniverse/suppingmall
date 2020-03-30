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
        Payment.PayGroupType paymentType = payment.getPaymentType();
        if(paymentType.equals(Payment.PayGroupType.CARD)) {
            payment.setStatus(Payment.PaymentStatus.COMPLETE);
        } else if(paymentType.equals(Payment.PayGroupType.CASH)) {
            payment.setStatus(Payment.PaymentStatus.BACKING_CHECKED);
        }
        paymentMapper.save(payment);
        return payment.getPaymentId();
    }

    public Payment findPayment(Long id) {
        return paymentMapper.findOne(id);
    }

    public List<Payment> findPayments() {
        return paymentMapper.findAll();
    }

    public void cancelPayment(Payment paymentId) {
        paymentMapper.updateStatus(paymentId);
    }
}
