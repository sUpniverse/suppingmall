package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.board.Board;

public interface BoardMapper {
    
    Board selectBoard(String id);

    String insertBoard(Board board);

    String updateBoard(String id, Board board);

    String deleteBoard(String id);
}
