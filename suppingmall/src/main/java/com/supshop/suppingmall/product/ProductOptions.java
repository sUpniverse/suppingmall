package com.supshop.suppingmall.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptions {

    private int id;
    private String optionName;
    private String price;
    private int totalCount;

}
