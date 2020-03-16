package com.supshop.suppingmall.delivery;


import lombok.*;

@Getter @Setter
@Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class Delivery {

    private Long deliveryId;
    private String name;
    private String address;
    private String addressDetail;
    private String zipCode;
    private String phone;
    private String status; // Todo : enum으로 OrderStatus
    private String vendor; // 배송사
    private String trackingNumber; // 송장번호


}
