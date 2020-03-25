package com.supshop.suppingmall.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class BoardPageMaker {

    private int totalCount;
    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;

    private static final int displayPageNum = 5;

    private BoardCriteria boardCriteria;

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        calcData();
    }

    private void calcData() {
        this.endPage = (int) (Math.ceil(boardCriteria.getPage() / (double) displayPageNum) * displayPageNum);
        this.startPage = (endPage - displayPageNum) + 1;

        int tempEndPage = (int) (Math.ceil(totalCount / (double) boardCriteria.perPageNum));

        if( this.endPage > tempEndPage) {
            this.endPage = tempEndPage;
        }

        this.prev = this.startPage == 1 ? false : true;

        this.next = this.endPage * boardCriteria.perPageNum >= totalCount ? false : true;
    }

}
