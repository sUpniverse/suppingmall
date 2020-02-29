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
    private PayGroup paymentType;

    //C : 카드결제
    private CardVendor vendor;          //vendor사 List로 가져와야함
    private int installmentMonth;
    private String checkNumber;     // vendor사 결제승인번호

    //B : 계좌이체
    private String accountNumber;

    private PaymentStatus status; // Todo: enum으로  C : (결제완료) , B : (입금대기, 입금확인, 환불완료) , 결제취소
    private LocalDateTime payedDate;
    private LocalDateTime updatedDate; // 결제에 따른 상태 변경날짜


    @Getter
    @AllArgsConstructor
    public enum PayGroup {

        CASH("계좌이체","P000"),
        CARD("카드","P001"),
        ETC("포인트","P002");

        private String title;
        private String code;

        public static PayGroup getCodeString(String code) {
            return Arrays.stream(PayGroup.values())
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
        WOORIE("우리카드","CD03");


        private String title;
        private String code;

        public static CardVendor getCodeString(String code) {
            return Arrays.stream(CardVendor.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }

    }

    @Getter
    @AllArgsConstructor
    public enum PaymentStatus {

        COMPLETE("결제완료","PS00"),
        WAITING("입금대기","PS01"),
        CHECKED("입금확인","PS02"),
        CANCEL("결제취소","PS03"),
        REFUNDED("환불완료","PS04");
        //C : (결제완료) , B : (입금대기, 입금확인, 환불완료) , 결제취소

        private String title;
        private String code;

        public static PaymentStatus getCodeString(String code) {
            return Arrays.stream(PaymentStatus.values())
                    .filter(v -> v.getCode().equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + code + "]"));
        }


    }

}
