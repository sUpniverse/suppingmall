package com.supshop.suppingmall.page;

import lombok.Getter;

@Getter
public class EightItemsCriteria extends Criteria {

    private static final int productPerPageNum = 8;

    public EightItemsCriteria() {
        super(productPerPageNum);
    }

    public void setPerPageNum(int perPageNum) {
        if(perPageNum > 0) this.setPerPageNum(perPageNum);
    }
}
