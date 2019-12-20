package com.supshop.suppingmall.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetail {

    private int id;
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

}
