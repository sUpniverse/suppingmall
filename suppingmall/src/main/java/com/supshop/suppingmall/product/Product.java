package com.supshop.suppingmall.product;

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

    int prductId;
    String name;
    String price;
    List<ProductOptions> optionsList;
    LocalDateTime registerDate;
    User seller;
    String brandName;
    String category;
    String thumbnail;
    String picture;
    String asNumber;
}
