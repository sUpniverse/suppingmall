package com.supshop.suppingmall.order;

import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class Order {

    private int id;
    private Product productId;
    private ProductOption productOptionId;
    private String name;
    private LocalDateTime ordered_date;
    private User buyer;
    private String price;
    private String deliverId;
    private String status; //Todo : enum
    private String orderType;


}
