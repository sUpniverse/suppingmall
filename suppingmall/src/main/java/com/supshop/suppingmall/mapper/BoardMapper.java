package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.page.Criteria;

import java.util.List;
import java.util.Optional;

public interface BoardMapper {

    List<Board> selectAllBoard();

    List<Board> selectBoardByCondition(Criteria boardCriteria, Long categoryId, String type, String searchValue);
    
    Optional<Board> selectBoard(Long id);

    int insertBoard(Board board);

    void updateBoard(Long id, Board board);

    void deleteBoard(Long id);

    int selectBoardCount(Long categoryId, String type, String searchValue);

    void updateBoardHit(Long id);

    List<Board> findBoardsByProductId(Long productId,Long categoryId);
}
