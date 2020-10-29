package com.supshop.suppingmall.product;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductSearch {

    private String name;
    private String categoryName;
    private int minPrice;
    private int maxPrice;
    private int rating;
}
