package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.board.Board;

import java.util.List;

public interface BoardMapper {

    List<Board> selectAllBoard();
    
    Board selectBoard(Long id);

    void insertBoard(Board board);

    void updateBoard(Long id, Board board);

    void deleteBoard(Long id);
}
