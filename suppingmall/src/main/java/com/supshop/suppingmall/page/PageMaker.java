package com.supshop.suppingmall.page;

import lombok.Getter;

@Getter
public class PageMaker {

    private int totalCount;
    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;
    private final int displayPageNum;        // 몇개의 페이지 정보가 보일 것인지
    private Criteria criteria;

    public PageMaker(int totalCount, int displayPageNum, Criteria criteria) {
        this.totalCount = totalCount;
        this.displayPageNum = displayPageNum;
        this.criteria = criteria;
        calcData();
    }

    private void calcData() {
        this.endPage = (int) (Math.ceil(this.criteria.getPage() / (double) this.displayPageNum) * this.displayPageNum);
        this.startPage = (endPage - displayPageNum) + 1;

        int tempEndPage = (int) (Math.ceil(this.totalCount / (double) this.criteria.getPerPageNum()));

        if( this.endPage > tempEndPage) {
            this.endPage = tempEndPage;
        }

        this.prev = this.startPage == 1 ? false : true;

        this.next = this.endPage * this.criteria.getPerPageNum() >= totalCount ? false : true;
    }

}
