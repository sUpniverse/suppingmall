package com.supshop.suppingmall.board;

import com.supshop.suppingmall.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    BoardMapper mapper;

    public List<Board> getAllBoard() { return mapper.selectAllBoard(); }

    public Board getBoard(String id) {
        return mapper.selectBoard(id);
    }

    public void createBoard(Board board) {
        mapper.insertBoard(board);
    }

    public void updateBoard(String id, Board board) {
        mapper.updateBoard(id, board);
    }

    public void deleteBoard(String id) {
        mapper.deleteBoard(id);
    }
}
