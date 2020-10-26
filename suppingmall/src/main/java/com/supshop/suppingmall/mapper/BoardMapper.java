package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.common.Search;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.ThirtyItemsCriteria;

import java.util.List;
import java.util.Optional;

public interface BoardMapper {

    int findCount(Search search);
    int findCountByUserId(Long userId, Search search);
    int findCountByCategoryId(Long categoryId,Search search);
    int findCountByParentCategoryId(Long parentCategoryId, Search search);

    List<Board> findAll(Criteria criteria,Search search);
    List<Board> findByUserId(Criteria criteria,Long userId, Search search);
    List<Board> findByCategoryId(Criteria criteria,Long categoryId, Search search);
    List<Board> findByParentCategoryId(Criteria criteria, Long parentCategoryId, Search search);

    Optional<Board> findOne(Long id);

    int save(Board board);

    void update(Long id, Board board);

    void delete(Long id);

    void updateBoardHit(Long id);

    void blind(Long id);
}
