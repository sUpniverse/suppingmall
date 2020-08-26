package com.supshop.suppingmall.page;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Criteria {

    private int page;
    private int perPageNum;    //페이지당 표시할 갯수

    public Criteria(int perPageNum) {
        this.page = 1;
        this.perPageNum = perPageNum;
    }

    public void setPage(int page) {
        if(page <= 0) {
            this.page = 1;
            return;
        }
        this.page = page;
    }

    public int getPageStart() {
        return (this.page - 1) * this.perPageNum;
    }

}
