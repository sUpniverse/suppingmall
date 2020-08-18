package com.supshop.suppingmall.board.form;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BoardCreateForm {

    private Long boardId;
    private String title;
    private String contents;
    private Set<String> imagesUrl;
    private User creator;
    private Category category;
    private int hit;
    private Product product;

}
