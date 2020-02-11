package com.supshop.suppingmall.product;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDetail {

    private int detailId;
    private String asNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate manuplatedDate;

    private String manufacturer;
    private String origin;
    private String spec1;
    private String spec2;
    private String spec3;
    private String spec4;
    private String spec5;
    private String spec6;

}
