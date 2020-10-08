package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.comment.Comment;
import com.supshop.suppingmall.page.Criteria;

import java.util.List;

public interface CommentMapper {

    int findCommentCount(Long boardId);
    List<Comment> findAll(Long boardId, Criteria criteria);
    int insertComment(Comment comment);
    int updateComment(Long id, Comment comment);
    int deleteComment(Long id);

}
