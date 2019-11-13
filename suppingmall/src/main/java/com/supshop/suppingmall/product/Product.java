package com.supshop.suppingmall.product;

import com.supshop.suppingmall.order.Order;
import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {

    private int productId;
    private String name;
    private String price;
    private List<ProductOptions> options;
    private List<Order> orders;
    private LocalDateTime registeredDate;
    private User sellerId;
    private String brandName;
    private String category;
    private String delYn;
    private String thumbnail;
    private String picture;
    private String asNumber;
    private String spec1;
    private String spec2;
    private String spec3;
    private String spec4;
    private String spec5;
}
