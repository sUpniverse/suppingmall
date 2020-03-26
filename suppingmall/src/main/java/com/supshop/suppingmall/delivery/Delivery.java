package com.supshop.suppingmall.delivery;


import com.supshop.suppingmall.order.Orders;
import lombok.*;

import java.util.Arrays;

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
    private String memo;
    private String vendor; // 배송사
    private String trackingNumber; // 송장번호
    private DeliveryStatus status; // 배송상태


    @Getter
    @AllArgsConstructor
    public enum DeliveryStatus {

        WAIT("배송준비중","DS00"),
        DELIVERY("배송중","DS01"),
        COMPLETE("배송완료","DS02");

        private String title;
        private String code;


        public static Delivery.DeliveryStatus getCode(String code) {
            if(code == null) {
                return null;
            }
            return Arrays.stream(Delivery.DeliveryStatus.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }

    }
}
