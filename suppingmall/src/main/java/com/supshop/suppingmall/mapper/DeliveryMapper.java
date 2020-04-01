package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.delivery.Delivery;

public interface DeliveryMapper {

    Delivery findOne(Long deliveryId);
    Long save(Delivery delivery);

    Long saveVendor(Long deliveryId,Delivery delivery);
}
