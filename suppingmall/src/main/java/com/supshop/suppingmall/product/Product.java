package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
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
    private String saleYn;
    private String thumbnail;
    private String picture;

}
