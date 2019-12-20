package com.supshop.suppingmall.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.user.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@ToString
public class Product {

    private Long productId;
    private String name;
    private String price;
    private ProductDetail detail;
    private List<ProductOption> options;
    private String contents;

    private LocalDateTime registeredDate;

    private User seller;
    private Category category;
    private String delYn;
    private String thumbnail;
    private String picture;

}
