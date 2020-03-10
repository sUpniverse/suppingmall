package com.supshop.suppingmall.order;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserVO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
@Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class Orders {

    private Long orderId;
    private List<OrderItem> orderItems;
    private LocalDateTime orderedDate;
    private UserVO buyer;
    private UserVO seller;
    private Payment payment;
    private Delivery delivery;
    private OrderStatus status; //Todo : enum (주문완료,배송,구매완료,취소)


    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Getter
    @AllArgsConstructor
    public enum  OrderStatus {

        ORDER("주문완료","O000"),
        CANCEL("주문취소","O001"),
        COMPLETE("구매확정","O002");

        private String title;
        private String code;


        public static OrderStatus getCode(String code) {

            return Arrays.stream(OrderStatus.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }

    }


}
