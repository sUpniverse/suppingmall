package com.supshop.suppingmall.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class PageMaker {

    private int totalCount;
    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;

    private static final int displayPageNum = 5;

    private Criteria criteria;

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        calcData();
    }

    private void calcData() {
        this.endPage = (int) (Math.ceil(criteria.getPage() / (double) displayPageNum) * displayPageNum);
        this.startPage = (endPage - displayPageNum) + 1;

        int tempEndPage = (int) (Math.ceil(totalCount / (double) criteria.perPageNum));

        if( this.endPage > tempEndPage) {
            this.endPage = tempEndPage;
        }

        this.prev = this.startPage == 1 ? false : true;

        this.next = this.endPage * criteria.perPageNum >= totalCount ? false : true;
    }

}
