package com.supshop.suppingmall.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Builder @ToString
@AllArgsConstructor
public class CardVO {

    //CARD : 카드결제
    private Payment.CardVendor vendor;          //vendor사 List로 가져와야함
    private String cardNumber;
    private int installmentMonth;
    private String vendorCheckNumber;     // vendor사 결제승인번호
}
