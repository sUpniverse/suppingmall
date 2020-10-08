package com.supshop.suppingmall.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDate;

}
