package com.supshop.suppingmall.board;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class Board {

    private Long boardId;
    private String title;
    private String contents;
    private User creator;
    private Category category;
    private String delYn;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
