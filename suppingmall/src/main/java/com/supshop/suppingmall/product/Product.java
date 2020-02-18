package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.Category;
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

    @NotEmpty
    private String name;


    @Min(0)
    @Max(1000000000)
    private int price;

    @Valid
    private ProductDetail detail;
    @Valid
    private List<ProductOption> options;

    @Max(5)
    private int rating;
    private String contents;

    private LocalDateTime registeredDate;

    private User seller;
    private Category category;
    private String saleYn;
    private String thumbnail;
    private String picture;

}
