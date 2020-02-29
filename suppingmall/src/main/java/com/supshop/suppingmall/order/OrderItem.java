package com.supshop.suppingmall.order;

import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import lombok.*;

@Setter @Getter
@Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class OrderItem {

    private Long orderItemId;
    private Order order;
    private Product product;
    private ProductOption productOption;
    private int count;
    private int orderPrice;
}
