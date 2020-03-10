package com.supshop.suppingmall.payment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Builder @ToString
@AllArgsConstructor
public class PayInfo {

    private String pg;
    private String pay_method;
    private String name;
    private int amount;
    private String currency;
    private String merchant_uid;
    private String buyer_email;
    private String buyer_name;
    private String buyer_tel;
    private String buyer_addr;
    private String buyer_postcode;
    private int[] card_quota;
    private long merchant_id;


}
