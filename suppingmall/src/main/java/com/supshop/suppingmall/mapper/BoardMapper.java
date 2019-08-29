package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.board.Board;

import java.util.List;

public interface BoardMapper {

    List<Board> selectAllBoard();
    
    Board selectBoard(String id);

    String insertBoard(Board board);

    String updateBoard(String id, Board board);

    String deleteBoard(String id);
}
