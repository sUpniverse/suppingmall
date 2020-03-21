package com.supshop.suppingmall.payModule;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder @ToString
public class Result implements Serializable {

    private String uid;
    private Long merchant_uid;
    private String apply_num;
    private String pay_method;
    private int paid_amount;
}
