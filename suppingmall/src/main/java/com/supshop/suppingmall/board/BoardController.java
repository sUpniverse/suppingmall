package com.supshop.suppingmall.board;

import com.supshop.suppingmall.board.form.BoardCreateForm;
import com.supshop.suppingmall.board.form.BoardUpdateForm;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.page.ThirtyItemsCriteria;
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
    public String form(Model model) {
        log.debug("'form'가 실행됨");
        List<Category> child = categoryService.getCategoryToGrandChildren(boardCategoryId).getChild();
        model.addAttribute("categories",child);
        return "/board/form";
    }

    @GetMapping("")
    public String getAllBoard(Model model,
                              ThirtyItemsCriteria thirtyItemsCriteria,
                              @RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) String searchValue) {

        log.debug("'getAllBoard'가 실행됨");
        if(categoryId == null) categoryId = boardCategoryId;

        int boardCount = boardService.getBoardCount(categoryId, type, searchValue);
        PageMaker pageMaker = new PageMaker(boardCount,boardDisplayPagingNum, thirtyItemsCriteria);
        List<Board> boards = boardService.getBoards(thirtyItemsCriteria, categoryId, type, searchValue);
        Category category = categoryService.getCategory(categoryId);

        model.addAttribute("boardList",boards);
        model.addAttribute("boardPageMaker", pageMaker);

        model.addAttribute("category", category);
        model.addAttribute("type", type);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("pageNum", thirtyItemsCriteria.getPage());

        return "/board/list";
    }

    @GetMapping("/{id}")
    public String getBoard(@PathVariable Long id,
                           ThirtyItemsCriteria thirtyItemsCriteria,
                           Model model,
                           @RequestParam(required = false) Long categoryId,
                           @RequestParam(required = false) String type,
                           @RequestParam(required = false) String searchValue) {
        log.debug("'getBoard'가 실행됨");

        Board board = boardService.getBoard(id);
        if(board == null) {
            return "redirect:/boards";
        }
        if(categoryId == null) categoryId = board.getCategory().getId();

        List<Board> boards = boardService.getBoards(thirtyItemsCriteria, board.getCategory().getId(), type, searchValue);
        int boardCount = boardService.getBoardCount(categoryId, type, searchValue);
        PageMaker pageMaker = new PageMaker(boardCount,boardDisplayPagingNum, thirtyItemsCriteria);

        model.addAttribute("boardList",boards);
        model.addAttribute("board",board);
        model.addAttribute("boardPageMaker", pageMaker);

        model.addAttribute("category", board.getCategory());
        model.addAttribute("type", type);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("pageNum", thirtyItemsCriteria.getPage());

        return "/board/board";
    }

    @PostMapping("")
    public String createBoard(BoardCreateForm createForm, @AuthenticationPrincipal SessionUser sessionUser) {
        log.debug("'createBoard'가 실행됨");

        if(!UserUtils.isOwner(createForm.getCreator().getUserId(), sessionUser)) return "redirect:/boards";

        Board board = modelMapper.map(createForm, Board.class);
        boardService.createBoard(board,createForm.getImagesUrl());
        return "redirect:/boards/"+board.getBoardId();
    }

    @GetMapping("/{id}/form")
    public String updateBoardForm(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {
        log.debug("'modifyBoard'가 실행됨");
        Board board = boardService.getBoard(id);
        if(!UserUtils.isOwner(board.getCreator().getUserId(), sessionUser)) return "redirect:/boards";

        model.addAttribute("board",board);
        return "/board/updateform";
    }

    @PutMapping("/{id}")
    public String updateBoard(@PathVariable Long id, BoardUpdateForm form, @AuthenticationPrincipal SessionUser sessionUser) {
        log.debug("'updateBoard'가 실행됨");
        Board board = boardService.getBoard(id);
        if(!id.equals(form.getBoardId()) || !UserUtils.isOwner(board.getCreator().getUserId(), sessionUser)) {
            return "redirect:/boards";
        }
        board.setTitle(form.getTitle());
        board.setContents(form.getContents());

        boardService.updateBoard(id, board,form.getImagesUrl());
        return "redirect:/boards/"+id;
    }

    @PostMapping("/{id}")
    public String deleteBoard(@PathVariable Long id, @AuthenticationPrincipal SessionUser sessionUser) {
        log.debug("'deleteBoard'가 실행됨");

        Board board = boardService.getBoard(id);
        if(!UserUtils.isOwner(board.getCreator().getUserId(), sessionUser)) {
            return "redirect:/boards";
        }
        boardService.deleteBoard(id);
        return "redirect:/boards";
    }

}
