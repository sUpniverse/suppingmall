package com.supshop.suppingmall.product;

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
    private LocalDateTime createdDate;


    public boolean isReplyExist() {
        if(this.reply == null) return false;
        return true;
    }
}