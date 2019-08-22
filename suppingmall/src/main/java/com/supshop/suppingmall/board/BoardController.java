package com.supshop.suppingmall.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/boards")
@RestController
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping("")
    public Board getBoard(@PathVariable String id) {
        return boardService.getBoard(id);
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
