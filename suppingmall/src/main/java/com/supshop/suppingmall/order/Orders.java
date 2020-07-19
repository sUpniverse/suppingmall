package com.supshop.suppingmall.order;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.user.SessionUser;
import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
@Builder @ToString
@NoArgsConstructor @AllArgsConstructor
public class Orders {

    private Long orderId;
    private OrderStatus status; //Todo : enum (주문완료,배송,구매완료,취소)
    private LocalDateTime orderDate;
    private LocalDateTime updateDate;
    private User buyer;
    private User seller;
    private Delivery delivery;
    private Payment payment;
    private List<OrderItem> orderItems = new ArrayList<>();


    @Getter
    @AllArgsConstructor
    public enum OrderStatus {

        WAIT("결제대기","O000"),
        ORDER("주문완료","O001"),
        DELIVERY("배송","O002"),
        COMPLETE("구매완료","O003"),
        CANCEL("취소완료","O004"),
        REFUND("반품요청","O005"),
        CHANGE("교환요청","O006");

        private String title;
        private String code;


        public static OrderStatus getCode(String code) {
            return Arrays.stream(Orders.OrderStatus.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }

    }

    // 임시주문 생성 (결제 시스템의 식별)
    public static Orders createTempOrder(List<OrderItem> orderItemList, User buyer, User seller) {
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

    public int getAmountProductPrice() {
        int price = 0;
        for(OrderItem orderItem : this.orderItems) {
            price += orderItem.getPrice();
        }
        return price;
    }

    public int getAmountPrice() {
        int deliveryPrice = getDeliveryPrice();
        return this.getAmountProductPrice() + deliveryPrice;
    }

    public int getDeliveryPrice() {
        return this.orderItems.get(0).getProduct().getDeliveryPrice();
    }

    public int getAmountProductCount() {
        int count = 0;
        for(OrderItem orderItem : this.orderItems) {
            count += orderItem.getCount();
        }
        return count;
    }


}
