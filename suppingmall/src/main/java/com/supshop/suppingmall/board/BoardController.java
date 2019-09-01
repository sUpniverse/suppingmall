package com.supshop.suppingmall.board;

import com.supshop.suppingmall.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequestMapping("/boards")
@Controller
@Slf4j
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping("/form/{category}")
    public String form(HttpSession session, Model model,@PathVariable String category) {
        log.debug("'form'가 실행됨");
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return "redirect:/users/loginform";
        }
        model.addAttribute("category",category);
        return "/board/form";
    }

    @GetMapping("")
    public String getAllBoard(Model model) {
        log.debug("'getAllBoard'가 실행됨");
        model.addAttribute(boardService.getAllBoard());
        return "/board/list";
    }

    @GetMapping("/{id}")
    public String getBoard(@PathVariable String id, Model model) {
        log.debug("'getBoard'가 실행됨");
        model.addAttribute("board",boardService.getBoard(id));
        return "/board/board";
    }

    @PostMapping("")
    public String createBoard(Board board, HttpSession session) {
        log.debug("'createBoard'가 실행됨");
        User user = (User)session.getAttribute("user");
        board.setCreater(user);
        boardService.createBoard(board);
        return "redirect:/boards";
    }

    @GetMapping("/{id}/form")
    public String modifyBoard(@PathVariable String id, Model model, HttpSession session) {
        log.debug("'getBoard'가 실행됨");
        Board board = boardService.getBoard(id);
        User user = (User)session.getAttribute("user");
        if(user == null || (user.getUserId() != board.getCreater().getUserId())) {
            return "redirect:/boards";
        }
        model.addAttribute("board",board);
        return "/board/updateform";
    }

    @PutMapping("/{id}")
    public String updateBoard(@PathVariable String id, Board board, HttpSession session) {
        log.debug("'updateBoard'가 실행됨");
        User user = (User)session.getAttribute("user");
        board.setCreater(user);
        boardService.updateBoard(id, board);
        return "redirect:/boards/"+id;
    }

    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable String id, HttpSession session) {
        log.debug("'deleteBoard'가 실행됨");
        User user = (User)session.getAttribute("user");
//        if(user.getUserId() != userId) {
//            return "redirect:/boards";
//        }
        boardService.deleteBoard(id);
        return "redirect:/boards";
    }

}
