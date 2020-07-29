package com.supshop.suppingmall.mail;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder @Getter
@ToString
public class Mail {

    private String to;
    private String subject;
    private String text;


}
