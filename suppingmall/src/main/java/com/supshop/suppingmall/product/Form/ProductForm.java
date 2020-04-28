package com.supshop.suppingmall.product.Form;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.product.ProductDetail;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter @Setter
public class ProductForm {

    @NotEmpty
    private String name;

    @Min(0)
    @Max(1000000000)
    private int price;

    @Min(0)
    @Max(1000000000)
    private int deliveryPrice;

    private Delivery.DeliveryVendor deliveryVendor;

    @Valid
    private ProductDetail detail;
    @Valid
    private List<ProductOption> options;

    private String contents;
    private User seller;
    private Category category;
    private String thumbnail;
    private String picture;
}
