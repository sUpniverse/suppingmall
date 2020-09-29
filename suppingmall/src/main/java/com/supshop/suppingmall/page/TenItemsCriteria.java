package com.supshop.suppingmall.page;

import lombok.Getter;

@Getter
public class TenItemsCriteria extends Criteria {

    private static final int PerPageNum = 10;

    public TenItemsCriteria() {
        super(PerPageNum);
    }

    public void setPerPageNum(int perPageNum) {
        if(perPageNum > 0) this.setPerPageNum(perPageNum);
    }
}
