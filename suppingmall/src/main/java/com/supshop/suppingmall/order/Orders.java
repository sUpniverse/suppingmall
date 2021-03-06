package com.supshop.suppingmall.order;

import com.supshop.suppingmall.delivery.Delivery;
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
    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime updateDate;
    private List<OrderItem> orderItems = new ArrayList<>();


    @Getter
    @AllArgsConstructor
    public enum OrderStatus {

        WAIT("결제대기","O000"),
        ORDER("주문완료","O001"),
        DELIVERY("배송","O002"),
        COMPLETE("구매확정","O003"),
        CANCEL("구매취소","O004"),
        REFUND("반품접수","O005"),
        CHANGE("교환접수","O006");

        private String title;
        private String code;


        public static OrderStatus getStatusByCode(String code) {
            return Arrays.stream(Orders.OrderStatus.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }

        public static String getCodeByEnumString(String value) {
            String code = null;
            return Arrays.stream(OrderStatus.values())
                    .filter(orderStatus -> orderStatus.toString().equals(value))
                    .findFirst()
                    .map(OrderStatus::getCode)
                    .orElse(null);
        }

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

    //물품 가격 + 배송비
    public int getAmountPrice() {
        return this.getAmountProductPrice() + getAmountDeliveryPrice();
    }

    public int getAmountDeliveryPrice() {
        int sum = this.getOrderItems().stream().mapToInt(orderItem -> orderItem.getProduct().getDeliveryPrice()).sum();
        return sum;
    }

    public int getAmountProductCount() {
        int count = this.orderItems.stream().mapToInt(OrderItem::getCount).sum();
        return count;
    }


}
