package com.supshop.suppingmall.delivery;


import com.supshop.suppingmall.order.OrderItem;
import lombok.*;

import java.util.Arrays;

@Getter @Setter
@Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class Delivery {


    //Todo : to , from 필요
    private Long deliveryId;
    private String name;
    private String address;
    private String addressDetail;
    private String zipCode;
    private String phone;
    private String memo;
    private OrderItem orderItem;
    private DeliveryVendor vendor; // 배송사
    private String trackingNumber; // 송장번호
    private DeliveryStatus status; // 배송상태


    @Getter
    @AllArgsConstructor
    public enum DeliveryStatus {

        WAIT("배송준비중","DS00"),
        DELIVERY("배송중","DS01"),
        COMPLETE("배송완료","DS02"),
        CHANGE("교환","DS03"),
        ;

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

    @Getter
    @AllArgsConstructor
    public enum DeliveryVendor{

        OFFICE("우체국택배","DV00"),
        CJ("대한통운","DV01"),
        HANJIN("한진택배","DV02"),
        LOGEN("로젠택배","DV03"),
        LOTTE("롯데택배","DV04");

        private String title;
        private String code;


        public static Delivery.DeliveryVendor getCode(String code) {
            if(code == null) {
                return null;
            }
            return Arrays.stream(Delivery.DeliveryVendor.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }
    }
}
