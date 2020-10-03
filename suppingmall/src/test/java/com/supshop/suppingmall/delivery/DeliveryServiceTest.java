package com.supshop.suppingmall.delivery;

import com.supshop.suppingmall.order.OrderFactory;
import com.supshop.suppingmall.order.OrderItem;
import com.supshop.suppingmall.order.Orders;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserFactory;
import com.supshop.suppingmall.user.UserService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DeliveryServiceTest {

    @Autowired DeliveryService deliveryService;
    @Autowired UserFactory userFactory;
    @Autowired OrderFactory orderFactory;


    @Test
    @Transactional
    public void saveDelivery() throws Exception {
        //given
        int size = deliveryService.getDeliveryList().size();
        User user = userFactory.createUser("user");
        Orders orders = orderFactory.createTempOrder();
        List<OrderItem> orderItems = orders.getOrderItems();

        String memo = "경비실에 맞겨주세요";
        List<Delivery> deliveryList = new ArrayList<>();
        Delivery delivery = Delivery.builder()
                .orderItem(orderItems.get(0))
                .name(user.getName())
                .phone(user.getPhoneNumber())
                .zipCode(user.getZipCode())
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .vendor(Delivery.DeliveryVendor.CJ)
                .memo(memo)
                .build();
        deliveryList.add(delivery);
        //when
        deliveryService.save(deliveryList);
        List<Delivery> addedDeliveryList = deliveryService.getDeliveryList();

        //then
        assertEquals(size+1,addedDeliveryList.size());
        for(Delivery findDelivery : addedDeliveryList) {
            assertEquals(delivery.getName(), findDelivery.getName());
            assertEquals(delivery.getPhone(), findDelivery.getPhone());
            assertEquals(delivery.getZipCode(), findDelivery.getZipCode());
            assertEquals(delivery.getAddress(), findDelivery.getAddress());
            assertEquals(delivery.getAddressDetail(), findDelivery.getAddressDetail());
            assertEquals(delivery.getVendor(), findDelivery.getVendor());
            assertEquals(delivery.getMemo(), findDelivery.getMemo());
        }
    }

}