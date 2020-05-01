package com.supshop.suppingmall.comment;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserVO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @Builder
@ToString @EqualsAndHashCode(of = "commentId")
@NoArgsConstructor @AllArgsConstructor
public class Comment {

    private Board board;
    private Long commentId;
    private int level;
    private String title;
    private String contents;
    private int score;
    private UserVO creator;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
