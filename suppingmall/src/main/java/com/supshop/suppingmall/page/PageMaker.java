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
    private BoardCriteria boardCriteria;

    public PageMaker(int totalCount, int displayPageNum, BoardCriteria boardCriteria) {
        this.totalCount = totalCount;
        this.displayPageNum = displayPageNum;
        this.boardCriteria = boardCriteria;
        calcData();
    }

    private void calcData() {
        this.endPage = (int) (Math.ceil(boardCriteria.getPage() / (double) displayPageNum) * displayPageNum);
        this.startPage = (endPage - displayPageNum) + 1;

        int tempEndPage = (int) (Math.ceil(totalCount / (double) this.boardCriteria.getPerPageNum()));

        if( this.endPage > tempEndPage) {
            this.endPage = tempEndPage;
        }

        this.prev = this.startPage == 1 ? false : true;

        this.next = this.endPage * this.boardCriteria.getPerPageNum() >= totalCount ? false : true;
    }

}
