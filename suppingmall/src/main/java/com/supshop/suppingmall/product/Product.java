package com.supshop.suppingmall.product;

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
    private LocalDateTime registerDate;
    private String sellerId;
    private String brandName;
    private String category;
    private String delYn;
    private String thumbnail;
    private String picture;
    private String asNumber;
}
