package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.comment.Comment;

import java.util.List;

public interface CommentMapper {

    List<Comment> selectAllComments(Long boardId);
    int insertComment(Comment comment);
    int updateComment(Long id, Comment comment);
    int deleteComment(Long id);
}
