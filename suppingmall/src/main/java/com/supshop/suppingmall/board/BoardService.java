package com.supshop.suppingmall.board;

import com.supshop.suppingmall.comment.CommentService;
import com.supshop.suppingmall.mapper.BoardMapper;
import com.supshop.suppingmall.page.Criteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BoardService {

    private BoardMapper boardMapper;
    private CommentService commentService;

    public BoardService(BoardMapper boardMapper, CommentService commentService) {
        this.boardMapper = boardMapper;
        this.commentService = commentService;
    }

    public List<Board> getAllBoard() { return boardMapper.selectAllBoard(); }

    public List<Board> getBoardByCondition(Criteria criteria, String type, String searchValue) {
        return boardMapper.selectBoardByCondition(criteria, type, searchValue);
    }

    public int getBoardCount() {
        return boardMapper.selectBoardCount();
    }

    public Board getBoard(Long id) {
        Optional<Board> board = boardMapper.selectBoard(id);
        if(!board.isEmpty()) {
            boardMapper.updateBoardHit(id);
            board.get().setComments(commentService.getAllComments(id));
            return board.get();
        }
        return null;
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
