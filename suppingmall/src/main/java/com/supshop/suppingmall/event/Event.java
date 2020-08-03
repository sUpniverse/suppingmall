package com.supshop.suppingmall.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Event {

    private EventType eventType;
    private LocalDateTime publishedTime;

}
