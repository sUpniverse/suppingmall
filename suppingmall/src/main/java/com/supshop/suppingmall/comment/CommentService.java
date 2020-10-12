package com.supshop.suppingmall.comment;

import com.supshop.suppingmall.mapper.CommentMapper;
import com.supshop.suppingmall.page.Criteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    public int getCommentCount(String type, String searchValue){
        return commentMapper.findCount(type,searchValue);
    }
    public int getCommentCountByBoardId(Long boardId, String type, String searchValue){
        return commentMapper.findCountByBoardId(boardId,type,searchValue);
    }
    public int getCommentCountByUserId(Long userId, String type, String searchValue){
        return commentMapper.findCountByUserId(userId,type,searchValue);
    }


    public List<Comment> getComments(Criteria criteria, String type, String searchValue) {
        return commentMapper.findAll(criteria,type,searchValue);
    }
    public List<Comment> getCommentsByUserId(Long userId, Criteria criteria, String type, String searchValue) {
        return commentMapper.findByUserId(userId, criteria, type, searchValue);
    }
    public List<Comment> getCommentsByBoardId(Long boardId, Criteria criteria, String type, String searchValue) {
        return commentMapper.findByBoardId(boardId,criteria,type,searchValue);
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
