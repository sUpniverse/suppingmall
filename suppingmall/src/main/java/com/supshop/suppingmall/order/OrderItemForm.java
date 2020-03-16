package com.supshop.suppingmall.order;

import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import lombok.*;

@Setter @Getter
@Builder @ToString
public class OrderItemForm {

    private Product product;
    private ProductOption productOption;
    private int count;
    private int price;
}
