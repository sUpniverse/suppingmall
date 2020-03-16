package com.supshop.suppingmall.order;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class TempOrder {

    private Long tempOrderId;
    private Long sellerId;
    private List<OrderItem> orderItemList = new ArrayList<>();      //Json String 파싱을 통해 변환된 주문내역

    public static TempOrder create(Long sellerId, List<OrderItem> orderItemList) {
        TempOrder tempOrder = new TempOrder();
        tempOrder.setSellerId(sellerId);
        tempOrder.setOrderItemList(orderItemList);
        return tempOrder;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItemList.add(orderItem);
    }

    public int getAmountPrice() {
        int price = 0;
        for(OrderItem orderItem : this.orderItemList) {
            price += orderItem.getPrice();
        }
        return price;
    }

    public int getAmountCount() {
        int count = 0;
        for(OrderItem orderItem : this.orderItemList) {
            count += orderItem.getCount();
        }
        return  count;
    }
}
