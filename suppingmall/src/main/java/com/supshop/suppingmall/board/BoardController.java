package com.supshop.suppingmall.board;

import com.supshop.suppingmall.board.form.BoardCreateForm;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.page.BoardCriteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.user.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    private static final Long boardCategoryId = 21l;
    private static final int boardDisplayPagingNum = 5;

    @GetMapping("/form")
    public String form(@RequestParam(required = false) Long categoryId,
                       Model model) {
        log.debug("'form'가 실행됨");
        List<Category> child = categoryService.getCategoryToGrandChildren(boardCategoryId).getChild();
        model.addAttribute("categories",child);
        return "/board/form";
    }

    @GetMapping("")
    public String getAllBoard(Model model,
                              BoardCriteria boardCriteria,
                              @RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) String searchValue) {

        log.debug("'getAllBoard'가 실행됨");
        if(categoryId == null) categoryId = boardCategoryId;

        int boardCount = boardService.getBoardCount(categoryId, type, searchValue);
        PageMaker pageMaker = new PageMaker(boardCount,boardDisplayPagingNum,boardCriteria);
        List<Board> boards = boardService.getBoardByCondition(boardCriteria, categoryId, type, searchValue);
        Category category = categoryService.getCategory(categoryId);

        model.addAttribute("boardList",boards);
        model.addAttribute("boardPageMaker", pageMaker);

        model.addAttribute("category", category);
        model.addAttribute("type", type);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("pageNum", boardCriteria.getPage());

        return "/board/list";
    }

    @GetMapping("/{id}")
    public String getBoard(@PathVariable Long id,
                           BoardCriteria boardCriteria,
                           Model model,
                           @RequestParam(required = false) Long categoryId,
                           @RequestParam(required = false) String type,
                           @RequestParam(required = false) String searchValue) {
        log.debug("'getBoard'가 실행됨");

        Board board = boardService.getBoard(id);
        if(board == null) {
            return "redirect:/boards";
        }
        if(categoryId == null) categoryId = boardCategoryId;

        List<Board> boards = boardService.getBoardByCondition(boardCriteria, board.getCategory().getId(), type, searchValue);
        int boardCount = boardService.getBoardCount(categoryId, type, searchValue);
        PageMaker pageMaker = new PageMaker(boardCount,boardDisplayPagingNum,boardCriteria);

        model.addAttribute("boardList",boards);
        model.addAttribute("board",board);
        model.addAttribute("boardPageMaker", pageMaker);

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("type", type);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("pageNum", boardCriteria.getPage());

        return "/board/board";
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
