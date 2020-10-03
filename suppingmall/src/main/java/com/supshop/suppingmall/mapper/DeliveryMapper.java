package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.delivery.Delivery;

import java.util.List;

public interface DeliveryMapper {

    List<Delivery> findAll();
    Delivery findOne(Long deliveryId);
    Long save(List<Delivery> delivery);
    Long saveVendor(Long deliveryId,Delivery delivery);
    int update(Delivery delivery);
    void delete(Delivery delivery);
}
