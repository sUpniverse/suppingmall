package com.supshop.suppingmall.board;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.comment.CommentService;
import com.supshop.suppingmall.mapper.BoardMapper;
import com.supshop.suppingmall.page.BoardCriteria;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    private final CommentService commentService;
    private final CategoryService categoryService;
    private static final String boardName = "board";

    public List<Board> getAllBoard() { return boardMapper.selectAllBoard(); }

    public List<Board> getBoardByCondition(BoardCriteria boardCriteria, String category, String type, String searchValue) {
        if(category == null) {
            Category categoryByEnName = categoryService.getCategoryByEnName(boardName);
            return boardMapper.selectBoardByCondition(boardCriteria,categoryByEnName.getId(), type, searchValue);
        }
        Category categoryByEnName = categoryService.getCategoryByEnName(category);
        return boardMapper.selectBoardByCondition(boardCriteria,categoryByEnName.getId(), type, searchValue);
    }

    public int getBoardCount() {
        return boardMapper.selectBoardCount();
    }

    public Board getBoard(Long id) {
        Optional<Board> board = boardMapper.selectBoard(id);
        if(!board.isEmpty()) {
            boardMapper.updateBoardHit(id);
            return board.get();
        }
        return null;
    }

    public Board getBoardByProduct(Long id) {
        Optional<Board> board = boardMapper.selectBoard(id);
        if(!board.isEmpty()) {
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

    public List<Board> getBoardsByProduct(Long productId,Long categoryId) {
        List<Board> boards = boardMapper.findBoardsByProductId(productId,categoryId);
        return boards;
    }
}
