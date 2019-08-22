package com.supshop.suppingmall.board;

import com.supshop.suppingmall.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class BoardService {

    @Autowired
    BoardMapper mapper;

    public Board getBoard(String id) {
        return mapper.selectBoard(id);
    }

    public String createBoard(Board board) {
        return mapper.insertBoard(board);
    }

    public String updateBoard(String id, Board board) {
        return mapper.updateBoard(id, board);
    }

    public String deleteBoard(String id) {
        return mapper.deleteBoard(id);
    }
}
