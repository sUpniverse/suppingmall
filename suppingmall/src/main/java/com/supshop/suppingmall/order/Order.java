package com.supshop.suppingmall.order;

import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOptions;
import com.supshop.suppingmall.user.User;

import java.time.LocalDateTime;

public class Order {

    private int id;
    private Product productId;
    private ProductOptions productOptionId;
    private String name;
    private LocalDateTime ordered_date;
    private User buyer;
    private String price;
    private String deliverId;
    private String status; //Todo : enum
    private String orderType;


}
