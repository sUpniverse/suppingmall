package com.supshop.suppingmall.order;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.user.User;
import lombok.*;

@Setter @Getter
@Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class OrderItem {

    private Long orderItemId;
    private Orders orders;
    private Product product;
    private ProductOption productOption;
    private int count;
    private int price;
    private User buyer;
    private User seller;
    private Delivery delivery;
    private Payment payment;
    private Orders.OrderStatus status;
    private String reviewYn;
}
