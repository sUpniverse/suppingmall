package com.supshop.suppingmall.comment;

import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.page.TenItemsCriteria;
import com.supshop.suppingmall.user.SessionUser;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity getComment(@RequestParam(required = false) Long boardId,
                                     @RequestParam(required = false) int page){
        Map<String,Object> map = new HashMap<>();

        int commentCount = commentService.getCommentCountByBoardId(boardId,null);

        Criteria criteria = new TenItemsCriteria();
        criteria.setPage(page);

        PageMaker pageMaker = new PageMaker(commentCount, 10, criteria);

        map.put("pageMaker", pageMaker);

        List<Comment> list = commentService.getCommentsByBoardId(boardId, criteria,null);
        map.put("list", list);

        return ResponseEntity.ok(map);
    }

    @PostMapping("")
    public ResponseEntity createComment(@RequestBody CommentForm commentForm, @AuthenticationPrincipal SessionUser sessionUser, Errors errors) {
        User user = userService.getUser(sessionUser.getUserId());
        Comment comment = modelMapper.map(commentForm, Comment.class);
        comment.setCreator(user);
        int result = commentService.insertComment(comment);

        if(result != 1) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id) {
        if(id != null) {
            commentService.deleteComment(id);
            return ResponseEntity.ok().build();
        }
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateComment(@PathVariable Long id, @RequestBody CommentForm commentForm) {
        if(id != null) {
            Comment comment = modelMapper.map(commentForm, Comment.class);
            int i = commentService.updateComment(id, comment);
            if(i == 1) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
