package com.supshop.suppingmall.page;

public class BoardPageMaker extends PageMaker{

    private static final int boardDisplayPageNum = 5;

    public BoardPageMaker(int totalCount, BoardCriteria boardCriteria) {
        super(totalCount, boardDisplayPageNum, boardCriteria);
    }
}
