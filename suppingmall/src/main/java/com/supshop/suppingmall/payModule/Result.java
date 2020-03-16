package com.supshop.suppingmall.payModule;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder @ToString
public class Result implements Serializable {

    private Long uid;
    private Long merchant_uid;
    private int paid_amount;
    private Long apply_num;
}
