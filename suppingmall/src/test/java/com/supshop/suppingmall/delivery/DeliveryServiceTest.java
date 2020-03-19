package com.supshop.suppingmall.delivery;

import com.supshop.suppingmall.user.UserService;
import com.supshop.suppingmall.user.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeliveryServiceTest {

    @Autowired
    DeliveryService deliveryService;
    @Autowired
    UserService userService;


    @Test
    public void saveDelivery() throws Exception {
        //given
        UserVO user = userService.getUserVO(2l);
        String vendor = "대한통운";
        String memo = "경비실에 맞겨주세요";
        Delivery delivery = Delivery.builder()
                .name(user.getName())
                .phone(user.getPhoneNumber())
                .zipCode(user.getZipCode())
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .vendor(vendor)
                .memo(memo)
                .build();
        //when
        Long deliveryId = deliveryService.save(delivery);
        Delivery findDelivery = deliveryService.findDelivery(deliveryId);

        //then
        assertEquals(delivery.getName(), findDelivery.getName());
        assertEquals(delivery.getPhone(), findDelivery.getPhone());
        assertEquals(delivery.getZipCode(), findDelivery.getZipCode());
        assertEquals(delivery.getAddress(), findDelivery.getAddress());
        assertEquals(delivery.getAddressDetail(), findDelivery.getAddressDetail());
        assertEquals(delivery.getVendor(), findDelivery.getVendor());
        assertEquals(delivery.getMemo(), findDelivery.getMemo());
    }

}