package com.supshop.suppingmall.comment;

import com.supshop.suppingmall.common.Search;
import com.supshop.suppingmall.mapper.CommentMapper;
import com.supshop.suppingmall.page.Criteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    public int getCommentCount(Search search){
        return commentMapper.findCount(search);
    }
    public int getCommentCountByBoardId(Long boardId, Search search){
        return commentMapper.findCountByBoardId(boardId,search);
    }
    public int getCommentCountByUserId(Long userId, Search search){
        return commentMapper.findCountByUserId(userId,search);
    }


    public List<Comment> getComments(Criteria criteria, Search search) {
        return commentMapper.findAll(criteria,search);
    }
    public List<Comment> getCommentsByUserId(Long userId, Criteria criteria, Search search) {
        return commentMapper.findByUserId(userId, criteria, search);
    }
    public List<Comment> getCommentsByBoardId(Long boardId, Criteria criteria, Search search) {
        return commentMapper.findByBoardId(boardId,criteria,search);
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
