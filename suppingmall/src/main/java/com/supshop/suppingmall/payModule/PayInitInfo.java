package com.supshop.suppingmall.payModule;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@ToString
public class PayInitInfo {

    //아임포트 결제모듈 기반의 모델
    private String pg;              //pg사
    private String pay_method;      //결제방법 (카드,실시간계좌,핸드폰)
    private Long merchant_uid;    //주문아이디 (주문식별자)
    private String name;            //사용자 이름
    private int amount;             //결제금액
    private String buyer_email;     //구매자 이메일
    private String buyer_name;      //구매자이름
    private String buyer_tel;       //구매자 핸드폰 번호
    private String buyer_addr;      //구매자 주소
    private String buyer_addr_detail;   //구매자 주소
    private String buyer_postcode;  //구매자 우편번호

}
