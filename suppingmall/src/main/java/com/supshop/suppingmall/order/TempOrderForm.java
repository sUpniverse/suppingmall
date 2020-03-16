package com.supshop.suppingmall.order;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TempOrderForm {

    private Long orderId;
    private Long productId;
    private Long sellerId;
    private Long buyerId;
    private String orderItems;                  //Json String 형태를 받기 위한 변수

}
