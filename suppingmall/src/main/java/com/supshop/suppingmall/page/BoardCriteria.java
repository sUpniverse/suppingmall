package com.supshop.suppingmall.page;

public class BoardCriteria extends Criteria{

    private static final int boardPerPageNum = 30;

    public BoardCriteria() {
        super(boardPerPageNum);
    }

    @Override
    public void setPerPageNum(int perPageNum) {
        if(perPageNum > 0) this.setPerPageNum(perPageNum);
    }
}
