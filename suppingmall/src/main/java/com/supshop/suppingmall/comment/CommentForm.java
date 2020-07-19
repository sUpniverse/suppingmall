package com.supshop.suppingmall.comment;

import com.supshop.suppingmall.board.Board;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(of = "commentId")
@NoArgsConstructor @AllArgsConstructor
public class CommentForm {

    private Board board;
    private Long commentId;
    private String title;
    private String contents;

}
