package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.user.User;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class Product {

    private Long productId;
    private String name;
    private int price;
    private int deliveryPrice;
    private Delivery.DeliveryVendor deliveryVendor;
    private ProductDetail detail;
    private List<ProductOption> options;
    private int rating;
    private String contents;
    private LocalDateTime registeredDate;
    private User seller;
    private Category category;
    private String saleYn;
    private String thumbnail;
    private String picture;
}
