package com.supshop.suppingmall.board;

import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board {

    private int boardId;
    private String title;
    private String contents;
    private String creater;
    private String category;
    private String delYn;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
