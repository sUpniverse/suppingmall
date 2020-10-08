package com.supshop.suppingmall.comment;

import com.supshop.suppingmall.mapper.CommentMapper;
import com.supshop.suppingmall.page.Criteria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    public int getCommentCount(Long boardId){
        return commentMapper.findCommentCount(boardId);
    }

    public List<Comment> getAllComments(Long boardId, Criteria criteria) {
        return commentMapper.findAll(boardId, criteria);
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
