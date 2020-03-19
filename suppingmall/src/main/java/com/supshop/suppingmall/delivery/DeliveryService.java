package com.supshop.suppingmall.delivery;

import com.supshop.suppingmall.mapper.DeliveryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryMapper deliveryMapper;

    public Delivery findDelivery(Long deliveryId) {
        return deliveryMapper.findOne(deliveryId);
    }

    public Long save(Delivery delivery) {
        deliveryMapper.save(delivery);
        return delivery.getDeliveryId();
    }
}
