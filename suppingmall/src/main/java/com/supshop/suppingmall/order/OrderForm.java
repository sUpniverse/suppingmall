package com.supshop.suppingmall.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OrderForm {

    private Long orderId;
    private Long productId;
    private Long sellerId;
    private String orderItems;                  //Json String 형태를 받기 위한 변수
    private List<OrderItem> orderItemList;      //Json String 파싱을 통해 변환된 주문내역
}
