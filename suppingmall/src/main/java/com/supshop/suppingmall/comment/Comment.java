package com.supshop.suppingmall.comment;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@ToString @EqualsAndHashCode(of = "commentId")
@NoArgsConstructor @AllArgsConstructor
public class Comment {

    private Long boardId;
    private Long commentId;
    private int level;
    private String title;
    private String contents;
    private int score;
    private User creator;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}