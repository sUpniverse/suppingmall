package com.supshop.suppingmall.page;

import lombok.Getter;

@Getter
public class OrderCriteria extends Criteria {

    private static final int orderPerPageNum = 10;

    public OrderCriteria() {
        super(orderPerPageNum);
    }

    public void setPerPageNum(int perPageNum) {
        if(perPageNum > 0) this.setPerPageNum(perPageNum);
    }
}
