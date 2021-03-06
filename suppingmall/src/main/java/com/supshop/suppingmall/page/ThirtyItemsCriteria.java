package com.supshop.suppingmall.page;

import lombok.Getter;

@Getter
public class ThirtyItemsCriteria extends Criteria{

    private static final int PerPageNum = 30;

    public ThirtyItemsCriteria() {
        super(PerPageNum);
    }

    @Override
    public void setPerPageNum(int perPageNum) {
        if(perPageNum > 0) this.setPerPageNum(perPageNum);
    }
}
