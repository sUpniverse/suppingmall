package com.supshop.suppingmall.board;

import com.supshop.suppingmall.board.form.BoardCreateForm;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.page.BoardCriteria;
import com.supshop.suppingmall.page.BoardPageMaker;
import com.supshop.suppingmall.user.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    private final Long boardCategoryId = 21l;



    @GetMapping("/form")
    public String form(@RequestParam(required = false) Long categoryId,
                       Model model) {
        log.debug("'form'가 실행됨");
        List<Category> child = categoryService.getCategory(boardCategoryId).getChild();
        model.addAttribute("categories",child);
        return "/board/form";
    }

    @GetMapping("")
    public String getAllBoard(Model model,
                              BoardCriteria boardCriteria,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) String searchValue) {

        log.debug("'getAllBoard'가 실행됨");

        BoardPageMaker boardPageMaker = new BoardPageMaker();
        boardPageMaker.setBoardCriteria(boardCriteria);
        boardPageMaker.setTotalCount(boardService.getBoardCount());
        model.addAttribute(boardService.getBoardByCondition(boardCriteria,category,type,searchValue));
        model.addAttribute("boardPageMaker", boardPageMaker);

        return "/board/list";
    }

    @GetMapping("/{id}")
    public String getBoard(@PathVariable Long id, Model model) {
        log.debug("'getBoard'가 실행됨");

        Board board = boardService.getBoard(id);
        if(board != null) {
            model.addAttribute("board",board);
            return "/board/board";
        }
        return "redirect:/boards";
    }

    @PostMapping("")
    public String createBoard(BoardCreateForm createForm, @AuthenticationPrincipal SessionUser sessionUser) {
        log.debug("'createBoard'가 실행됨");

        if(sessionUser.getUserId().equals(createForm.getCreator().getUserId())) {
            Board board = modelMapper.map(createForm, Board.class);
            boardService.createBoard(board,createForm.getImagesUrl());
        }
        return "redirect:/boards";
    }

    @GetMapping("/{id}/form")
    public String updateBoardForm(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {
        log.debug("'modifyBoard'가 실행됨");
        Board board = boardService.getBoard(id);
        if(!sessionUser.getUserId().equals(board.getCreator().getUserId())) return "redirect:/boards";

        model.addAttribute("board",board);
        return "/board/updateform";
    }

    @PutMapping("/{id}")
    public String updateBoard(@PathVariable Long id, Board board, @AuthenticationPrincipal SessionUser sessionUser) {
        log.debug("'updateBoard'가 실행됨");
        if(!UserUtils.isOwner(id, sessionUser)) return "redirect:/boards";
        boardService.updateBoard(id, board);
        return "redirect:/boards/"+id;
    }

    @PostMapping("/{id}")
    public String deleteBoard(@PathVariable Long id, @AuthenticationPrincipal SessionUser sessionUser) {
        log.debug("'deleteBoard'가 실행됨");

        Board board = boardService.getBoard(id);
        if(sessionUser.getUserId() != board.getCreator().getUserId()) {
            return "redirect:/boards";
        }
        boardService.deleteBoard(id);
        return "redirect:/boards";
    }

}
