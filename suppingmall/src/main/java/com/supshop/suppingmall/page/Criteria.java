package com.supshop.suppingmall.page;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Criteria {

    private int page;
    public static final int perPageNum = 10;

    public Criteria() {
        this.page = 1;
    }

    public void setPage(int page) {
        if(page <= 0) {
            this.page = 1;
            return;
        }

        this.page = page;
    }

    public int getPageStart() {
        return (this.page - 1) * perPageNum;
    }

}
