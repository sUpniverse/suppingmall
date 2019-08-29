package com.supshop.suppingmall.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequestMapping("/boards")
@Controller
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping("/form")
    public String form(HttpSession session) {

        return "/board/form";
    }

    @GetMapping("")
    public String getAllBoard(Model model) {
        model.addAttribute(boardService.getAllBoard());
        return "/board/list";
    }

    @GetMapping("/{id}")
    public String getBoard(@PathVariable String id, Model model) {
        model.addAttribute(boardService.getBoard(id));
        return "/board/board";
    }

    @PostMapping("")
    public String createBoard(Board board) {
        return boardService.createBoard(board);
    }

    @PutMapping("")
    public String updateBoard(@PathVariable String id, Board board) {
        return boardService.updateBoard(id, board);
    }

    @DeleteMapping("")
    public String deleteBoard(@PathVariable String id) {
        return boardService.deleteBoard(id);
    }

}
