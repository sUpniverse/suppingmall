package com.supshop.suppingmall.board;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.comment.Comment;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserVO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@ToString @Builder
@NoArgsConstructor @AllArgsConstructor
public class Board {

    private Long boardId;
    private String title;
    private String contents;
    private List<Comment> comments;
    private UserVO creator;
    private Category category;
    private int hit;
    private String delYn;
    private Product product;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
