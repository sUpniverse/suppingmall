package com.supshop.suppingmall.comment;

import com.supshop.suppingmall.common.SessionUtils;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserVO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public ResponseEntity createComment(@RequestBody Comment comment, HttpSession session, Errors errors) {
        UserVO sessionUser = SessionUtils.getSessionUser(session);
        comment.setCreator(sessionUser);
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
    public ResponseEntity updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        if(id != null) {
            int i = commentService.updateComment(id, comment);
            if(i == 1) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
