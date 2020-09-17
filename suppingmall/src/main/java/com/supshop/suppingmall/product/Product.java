package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.order.Orders;
import com.supshop.suppingmall.user.User;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class Product {

    private Long productId;
    private String name;
    private int price;
    private int deliveryPrice;
    private Delivery.DeliveryVendor deliveryVendor;
    private ProductDetail detail;
    private List<ProductOption> options;
    private int rating;
    private String contents;
    private LocalDateTime registeredDate;
    private User seller;
    private Category category;
    private ProductStatus status;
    private String thumbnail;
    private String picture;


    @Getter
    @AllArgsConstructor
    public enum  ProductStatus {

        WAIT("판매대기","PS00"),
        SALE("판매중","PS01"),
        STOP("판매중지","PS02"),
        SOLDOUT("품절","PS03"),
        END("판매종료","PS04"),
        DELETED("삭제","PS05");

        private String title;
        private String code;


        public static Product.ProductStatus getCode(String code) {
            return Arrays.stream(Product.ProductStatus.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }
    }
}
