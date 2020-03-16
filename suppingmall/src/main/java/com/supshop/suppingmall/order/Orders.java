package com.supshop.suppingmall.order;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.user.UserVO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
@Builder @ToString
public class Orders {

    private Long orderId;
    private List<OrderItem> orderItems = new ArrayList<>();
    private UserVO buyer;
    private UserVO seller;
    private Payment payment;
    private Delivery delivery;
    private OrderStatus status; //Todo : enum (주문완료,배송,구매완료,취소)
    private LocalDateTime orderDate;
    private LocalDateTime updateDate;

    @Getter
    @AllArgsConstructor
    public enum  OrderStatus {

        WAIT("결제대기","O000"),
        ORDER("주문완료","O001"),
        CANCEL("주문취소","O002"),
        COMPLETE("구매확정","O003");

        private String title;
        private String code;


        public static OrderStatus getCode(String code) {

            return Arrays.stream(OrderStatus.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }

    }

    //임시 주문아이디 생성 (결제 시스템의 식별)
    public static Orders createTempOrder(List<OrderItem> orderItemList, UserVO buyer, UserVO seller) {
        Orders orders = Orders.builder()
                            .buyer(buyer)
                            .seller(seller)
                            .orderItems(orderItemList)
                            .orderDate(LocalDateTime.now())
                            .orderDate(LocalDateTime.now())
                            .status(OrderStatus.WAIT)
                            .build();
        return orders;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public int getAmountPrice() {
        int price = 0;
        for(OrderItem orderItem : this.orderItems) {
            price += orderItem.getPrice();
        }
        return price;
    }

    public int getAmountCount() {
        int count = 0;
        for(OrderItem orderItem : this.orderItems) {
            count += orderItem.getCount();
        }
        return  count;
    }
}
