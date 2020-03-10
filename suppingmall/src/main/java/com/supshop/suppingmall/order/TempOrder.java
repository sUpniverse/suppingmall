package com.supshop.suppingmall.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class TempOrder {

    private Long tempOrderId;
    private Long productId;
    private Long sellerId;
    private String orderItems;
    private List<OrderItem> orderItemList;
    private int amountCount;
    private int amountPrice;


    public void setAmountPrice() {
        int price = 0;
        for(OrderItem orderItem : this.orderItemList) {
            price += orderItem.getPrice();
        }
        this.amountPrice = price;
    }

    public void setAmountCount() {
        int count = 0;
        for(OrderItem orderItem : this.orderItemList) {
            count += orderItem.getCount();
        }
        this.amountCount = count;
    }
}
