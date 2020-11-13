package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.order.Orders;
import com.supshop.suppingmall.review.Review;
import com.supshop.suppingmall.user.User;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class Product {

    private Long productId;
    private String name;
    private int price;
    private int deliveryPrice;
    private Category category;
    private String contents;
    private String thumbnail;
    private ProductDetail detail;
    private List<ProductOption> options;
    private Delivery.DeliveryVendor deliveryVendor;
    private List<QnA> qnAList;
    private List<Review> reviewList;
    private User seller;
    private ProductStatus status;
    private LocalDateTime registeredDate;
    private double rating;

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

//    public void setRating() {
//        if(reviewList == null)
//            this.rating =  0;
//        else
//            this.rating = reviewList.stream().collect(Collectors.averagingInt(Review::getRating));
//    }
//
//    public double getRating() {
//        setRating();
//        return rating;
//    }
}
