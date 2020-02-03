package com.supshop.suppingmall.category;

import lombok.*;

import java.util.List;

@Getter @Setter
@ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class Category {

    private Long id;
    private Category parent;
    private String name;
    private List<Category> child;


}
