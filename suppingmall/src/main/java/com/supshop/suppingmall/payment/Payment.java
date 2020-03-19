package com.supshop.suppingmall.payment;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@Getter @Setter
@Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class Payment {

    private Long paymentId;
    private int price;
    private PayGroupType paymentType;

    private String vendorCheckNumber;

    private PaymentStatus status;
    private LocalDateTime payDate;
    private LocalDateTime updatedDate; // 결제에 따른 상태 변경날짜


    @Getter
    @AllArgsConstructor
    public enum PayGroupType {

        CASH("현금","PG00"),
        CARD("카드","PG01"),
        ETC("포인트","PG02");

        private String title;
        private String code;

        public static PayGroupType getCodeString(String code) {
            if(code == null) {
                return null;
            }
            return Arrays.stream(PayGroupType.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }

    }

    @Getter
    @AllArgsConstructor
    public enum CardVendor {

        SAMSUNG("삼성카드","CD00"),
        HYUNDAI("현대카드","CD01"),
        KUKMIN("국민카드","CD02"),
        WOORIE("우리카드","CD03"),
        BC("BC카드","CD03");


        private String title;
        private String code;

        public static CardVendor getCodeString(String code) {
            if(code == null) {
                return null;
            }
            return Arrays.stream(CardVendor.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }

    }

    @Getter
    @AllArgsConstructor
    public enum PaymentStatus {

        WAITING("결제대기","PS00"),
        COMPLETE("결제완료","PS01"),
        BACKING_WAITING("입금대기","PS02"),
        BACKING_CHECKED("입금확인","PS03"),
        CANCEL("결제취소","PS04"),
        REFUND  ("환불완료","PS05");
        //C : (결제완료,결제취소) , B : (입금대기, 입금확인,결제취소, 환불완료)

        private String title;
        private String code;

        public static PaymentStatus getCodeString(String code) {
            if(code == null) {
                return null;
            }
            return Arrays.stream(PaymentStatus.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }


    }

}
