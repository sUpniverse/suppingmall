package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.comment.Comment;
import com.supshop.suppingmall.page.Criteria;

import java.util.List;

public interface CommentMapper {

    int findCount(String type, String searchValue);
    int findCountByBoardId(Long boardId,String type, String searchValue);
    int findCountByUserId(Long userId, String type, String searchValue);

    List<Comment> findAll(Criteria criteria, String type, String searchValue);
    List<Comment> findByBoardId(Long boardId,Criteria criteria, String type, String searchValue);
    List<Comment> findByUserId(Long userId, Criteria criteria, String type, String searchValue);

    int insertComment(Comment comment);
    int updateComment(Long id, Comment comment);
    int deleteComment(Long id);

}
