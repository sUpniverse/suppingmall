package com.supshop.suppingmall.comment;

import com.supshop.suppingmall.mapper.CommentMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class CommentService {

    private CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<Comment> getAllComments(Long boardId) {
        return commentMapper.selectAllComments(boardId);
    }

    public int insertComment(Comment comment) {
        return commentMapper.insertComment(comment);
    }

    public int updateComment(Long id, Comment comment) {
        return commentMapper.updateComment(id, comment);
    }

    public int deleteComment(Long id) {
        return commentMapper.deleteComment(id);
    }


}
