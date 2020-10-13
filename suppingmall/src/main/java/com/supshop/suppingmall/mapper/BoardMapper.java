package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.ThirtyItemsCriteria;

import java.util.List;
import java.util.Optional;

public interface BoardMapper {

    int findCount(String type, String searchValue);
    int findCountByUserId(Long userId, String type, String searchValue);
    int findCountByCategoryId(Long categoryId,String type, String searchValue);
    int findCountByParentCategoryId(Long parentCategoryId, String type, String searchValue);

    List<Board> findAll(Criteria criteria,String type, String searchValue);
    List<Board> findByUserId(Criteria criteria,Long userId, String type, String searchValue);
    List<Board> findByCategoryId(Criteria criteria,Long categoryId, String type, String searchValue);
    List<Board> findByParentCategoryId(Criteria criteria, Long parentCategoryId, String type, String searchValue);

    Optional<Board> findOne(Long id);

    int save(Board board);

    void update(Long id, Board board);

    void delete(Long id);

    void updateBoardHit(Long id);

}
