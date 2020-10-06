package com.supshop.suppingmall.review;

import com.supshop.suppingmall.order.OrderItem;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder @ToString @EqualsAndHashCode(of = "reviewId")
@AllArgsConstructor @NoArgsConstructor
public class Review {

    private Long reviewId;
    private String title;
    private String contents;
    private int rating;
    private Product product;
    private OrderItem orderItem;
    private User creator;
    private LocalDateTime createdDate;

}
