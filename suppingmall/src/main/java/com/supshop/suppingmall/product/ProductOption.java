package com.supshop.suppingmall.product;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class ProductOption {

    private Long productId;
    private int optionId;

    @NotEmpty
    private String optionName;

    @Min(0)
    @Max(1000000000)
    private int price;

    @Min(0)
    private int quantity;

    /**
     * stock 증가
     * @param quantity
     */
    public void addStock(int quantity) {
        this.quantity += quantity;
    }

    /**
     * stock 감소
     * @param quantity
     */
    public void removeStock(int quantity) {
        int restStock = this.quantity - quantity;
        if(restStock < 0) {
            throw new RuntimeException("need more Stock");
        }
        this.quantity = restStock;
    }

}
