package com.supshop.suppingmall.product;

import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class ProductOption {

    private Long productId;
    private int optionId;
    private String optionName;
    private String price;
    private int quantity;

}
