package com.supshop.suppingmall.page;

import lombok.Getter;

@Getter
public class ProductCriteria extends Criteria {

    private static final int productPerPageNum = 12;

    public ProductCriteria() {
        super(productPerPageNum);
    }

    @Override
    public void setPerPageNum(int perPageNum) {
        if(perPageNum <= 0) this.setPerPageNum(productPerPageNum);
        else this.setPerPageNum(perPageNum);
    }
}
