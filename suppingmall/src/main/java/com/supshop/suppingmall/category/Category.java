package com.supshop.suppingmall.category;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class Category {

    private Long id;
    private Category parent;
    private String name;        // 한글이름
    private String enName;      // 영문이름
    private List<Category> child;
    private LocalDateTime createDate;
    private String memo;

}
