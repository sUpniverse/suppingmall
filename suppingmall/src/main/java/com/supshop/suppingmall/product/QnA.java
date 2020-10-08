package com.supshop.suppingmall.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class QnA {

    private Long qnaId;
    private String title;
    private User creator;
    private Product product;
    private QnA reply;
    private QnA parent;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDate;


    public boolean isReplyExist() {
        if(this.reply == null) return false;
        return true;
    }
}
