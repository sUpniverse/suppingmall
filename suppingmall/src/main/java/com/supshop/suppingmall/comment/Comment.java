package com.supshop.suppingmall.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;

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
    private User creator;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
