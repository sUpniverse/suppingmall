package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.comment.Comment;
import com.supshop.suppingmall.common.Search;
import com.supshop.suppingmall.page.Criteria;

import java.util.List;

public interface CommentMapper {

    int findCount(Search search);
    int findCountByBoardId(Long boardId,Search search);
    int findCountByUserId(Long userId, Search search);

    List<Comment> findAll(Criteria criteria, Search search);
    List<Comment> findByBoardId(Long boardId,Criteria criteria, Search search);
    List<Comment> findByUserId(Long userId, Criteria criteria, Search search);

    int insertComment(Comment comment);
    int updateComment(Long id, Comment comment);
    int deleteComment(Long id);

}
