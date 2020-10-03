package com.supshop.suppingmall.product.form;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;


@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString @Builder
public class OptionForm {

    private Long productId;
    private int optionId;

    @NotEmpty
    private String optionName;

    @Min(0)
    @Max(1000000000)
    private int price;

    @Min(0)
    private int quantity;

}
