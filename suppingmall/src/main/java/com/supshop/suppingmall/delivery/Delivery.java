package com.supshop.suppingmall.delivery;


import com.supshop.suppingmall.order.OrderItem;
import lombok.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter @Setter
@Builder @ToString @EqualsAndHashCode(of = "deliveryId")
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
        REFUND("반품","DS03"),
        CHANGE("교환","DS04"),
        ;

        private String title;
        private String code;
        public static final Map<String,String> mapValues = new HashMap<>();

        static {
            Arrays.stream(values()).forEach(status -> mapValues.put(status.toString(), status.getTitle()));
        }


        public static DeliveryStatus getStatusByCode(String code) {
            if(code == null) {
                return null;
            }
            return Arrays.stream(DeliveryStatus.values())
                    .filter(v -> v.code.equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }

        public static String getCodeByEnumString(String value) {
            String code = null;
            return Arrays.stream(DeliveryStatus.values())
                    .filter(deliveryStatus -> deliveryStatus.toString().equals(value))
                    .findFirst()
                    .map(DeliveryStatus::getCode)
                    .orElse(null);
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
