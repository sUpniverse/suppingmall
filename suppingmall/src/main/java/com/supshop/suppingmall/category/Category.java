package com.supshop.suppingmall.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
public class Category {

    private Long id;
    private Category parent;
    private String name;
    private List<Category> child;


}
