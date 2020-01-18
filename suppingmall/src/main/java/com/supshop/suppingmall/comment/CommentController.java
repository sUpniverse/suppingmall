package com.supshop.suppingmall.comment;

import com.supshop.suppingmall.user.User;
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
        System.out.println(comment);
        User user = (User)session.getAttribute("user");
        comment.setCreator(user);
        int result = commentService.insertComment(comment);

        if(result != 1) {
            return ResponseEntity.badRequest().build();
        }
//        linkTo((CommentController.class)).slash()

        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteComment(@PathVariable String id) {
        if(id != null) {
            System.out.println(id);
            return ResponseEntity.ok().build();
        }
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        if(id != null) {
            commentService.updateComment(id, comment);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id]")
    public ResponseEntity deleteComment(@PathVariable Long id) {
        if(id != null) {
            commentService.deleteComment(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
