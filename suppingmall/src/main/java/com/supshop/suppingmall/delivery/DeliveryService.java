package com.supshop.suppingmall.delivery;

import com.supshop.suppingmall.mapper.DeliveryMapper;
import com.supshop.suppingmall.order.OrderService;
import com.supshop.suppingmall.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryMapper deliveryMapper;

    public List<Delivery> getDeliveryList() {
        return deliveryMapper.findAll();
    }

    public Delivery getDelivery(Long deliveryId) {
        return deliveryMapper.findOne(deliveryId);
    }

    @Transactional
    public void save(List<Delivery> deliveryList) {
        deliveryMapper.save(deliveryList);
    }

    @Transactional
    public Long update(Delivery delivery) {
        deliveryMapper.update(delivery);
        return delivery.getDeliveryId();
    }

    @Transactional
    public Long saveVendor(Long id,Delivery delivery) {
        // 택배사 등록 및 배송상태 변경
        delivery.setStatus(Delivery.DeliveryStatus.DELIVERY);
        deliveryMapper.saveVendor(id,delivery);
        return delivery.getDeliveryId();
    }

    @Transactional
    public void delete(Delivery delivery) {
        //결제 취소시 배송기록 삭제
        deliveryMapper.delete(delivery);
    }


}
