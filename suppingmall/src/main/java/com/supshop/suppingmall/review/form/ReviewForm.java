package com.supshop.suppingmall.review.form;

import lombok.*;

@Getter @Setter
@Builder @ToString
@NoArgsConstructor @AllArgsConstructor
public class ReviewForm {

    private Long userId;
    private String title;
    private String contents;
    private int rating;
    private Long orderItemId;

}
