package com.supshop.suppingmall.board;

import com.supshop.suppingmall.comment.CommentService;
import com.supshop.suppingmall.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private BoardMapper boardMapper;
    private CommentService commentService;

    public BoardService(BoardMapper boardMapper, CommentService commentService) {
        this.boardMapper = boardMapper;
        this.commentService = commentService;
    }

    public List<Board> getAllBoard() { return boardMapper.selectAllBoard(); }

    public Board getBoard(Long id) {
        Board board = boardMapper.selectBoard(id);
        board.setComments(commentService.getAllComments(id));
        return board;
    }

    public void createBoard(Board board) {
        boardMapper.insertBoard(board);
    }

    public void updateBoard(Long id, Board board) {
        boardMapper.updateBoard(id, board);
    }

    public void deleteBoard(Long id) {
        boardMapper.deleteBoard(id);
    }
}
