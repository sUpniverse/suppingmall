package com.supshop.suppingmall.comment;

import com.supshop.suppingmall.user.SessionUser;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private CommentService commentService;
    private UserService userService;
    private ModelMapper modelMapper;

    public CommentController(CommentService commentService, UserService userService, ModelMapper modelMapper) {
        this.commentService = commentService;
        this.userService = userService;
        this.modelMapper = modelMapper;
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
