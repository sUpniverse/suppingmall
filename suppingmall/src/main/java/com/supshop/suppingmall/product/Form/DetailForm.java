package com.supshop.suppingmall.product.Form;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;


@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString @Builder
public class DetailForm {

    private Long productId;
    private int detailId;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
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
