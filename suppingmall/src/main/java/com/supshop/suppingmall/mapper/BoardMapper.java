package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.board.Board;

import java.util.List;

public interface BoardMapper {

    List<Board> selectAllBoard();
    
    Board selectBoard(String id);

    void insertBoard(Board board);

    void updateBoard(String id, Board board);

    void deleteBoard(String id);
}
