package com.supshop.suppingmall.board;

import com.supshop.suppingmall.board.form.BoardCreateForm;
import com.supshop.suppingmall.board.form.BoardUpdateForm;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.comment.Comment;
import com.supshop.suppingmall.comment.CommentService;
import com.supshop.suppingmall.common.Search;
import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.page.TenItemsCriteria;
import com.supshop.suppingmall.page.ThirtyItemsCriteria;
import com.supshop.suppingmall.user.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final CommentService  commentService;

    private static final Long boardCategoryId = 21l;
    private static final int boardDisplayPagingNum = 10;

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
                              Search search,
                              @RequestParam(required = false, defaultValue = "farm") String categoryName){

        log.debug("'getAllBoard'가 실행됨");
        Category category = categoryService.getCategoryByEnName(categoryName);

        int boardCount = boardService.getBoardCountByCategoryId(category.getId(), search);
        List<Board> boards = boardService.getBoardsByCategoryId(thirtyItemsCriteria, category.getId(), search);
        PageMaker pageMaker = new PageMaker(boardCount,boardDisplayPagingNum, thirtyItemsCriteria);

        model.addAttribute("boardList",boards);
        model.addAttribute("boardPageMaker", pageMaker);

        model.addAttribute("category", category);
        model.addAttribute("type", search.getType());
        model.addAttribute("searchValue", search.getSearchValue());
        model.addAttribute("pageNum", thirtyItemsCriteria.getPage());

        return "/board/list";
    }

    @GetMapping("/{id}")
    public String getBoard(@PathVariable Long id,
                           ThirtyItemsCriteria thirtyItemsCriteria,
                           Model model,
                           @RequestParam(required = false) Long categoryId,
                           Search search) {
        log.debug("'getBoard'가 실행됨");

        Board board = boardService.getBoard(id);
        if(board == null) {
            return "redirect:/boards";
        }
        if(categoryId == null) categoryId = board.getCategory().getId();

        List<Board> boards = boardService.getBoardsByCategoryId(thirtyItemsCriteria, board.getCategory().getId(), search);
        int boardCount = boardService.getBoardCountByCategoryId(categoryId, search);
        PageMaker pageMaker = new PageMaker(boardCount,boardDisplayPagingNum, thirtyItemsCriteria);

        int commentCount = commentService.getCommentCountByBoardId(id,null);
        Criteria criteria = new TenItemsCriteria();
        PageMaker commentPageMaker = new PageMaker(commentCount,boardDisplayPagingNum,criteria);

        model.addAttribute("boardList",boards);
        model.addAttribute("board",board);
        model.addAttribute("boardPageMaker", pageMaker);
        model.addAttribute("commentPageMaker",commentPageMaker);

        model.addAttribute("category", board.getCategory());
        model.addAttribute("pageNum", thirtyItemsCriteria.getPage());

        return "/board/board";
    }

    @PostMapping("")
    public String createBoard(BoardCreateForm createForm, @AuthenticationPrincipal SessionUser sessionUser) throws FileNotFoundException {
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
        Board board = boardService.getBoard(id);
        if(!UserUtils.isOwner(board.getCreator().getUserId(), sessionUser)) {
            throw new AccessDeniedException("권한이 없는 유저입니다.");
        }
        boardService.deleteBoard(id);
        return "redirect:/boards";
    }

    @PostMapping("/{id}/blind")
    public String blindBoard(@PathVariable Long id) {
        boardService.blindBoard(id);
        return "redirect:/boards";
    }

    @GetMapping("/main")
    public String getMyBoard(@RequestParam(required = false) String category,
                             Search search,
                             TenItemsCriteria criteria,
                             @AuthenticationPrincipal SessionUser sessionUser,
                             Model model){

        PageMaker pageMaker = null;
        if(category == null)
            category = "boards";

        if("boards".equals(category)) {
            int boardCount = boardService.getBoardCountByUserId(sessionUser.getUserId(), search);
            List<Board> boardList = boardService.getBoardByUserId(criteria, sessionUser.getUserId(), search);
            pageMaker = new PageMaker(boardCount,boardDisplayPagingNum, criteria);

            model.addAttribute("boardList",boardList);
        } else if("comments".equals(category)){
            int commentCount = commentService.getCommentCountByUserId(sessionUser.getUserId(), search);
            List<Comment> commentList = commentService.getCommentsByUserId(sessionUser.getUserId(), criteria, search);
            pageMaker = new PageMaker(commentCount, boardDisplayPagingNum, criteria);

            model.addAttribute("commentList",commentList);
        } else if("like".equals(category)){

        }

        model.addAttribute("category",category);
        model.addAttribute("type",search.getType());
        model.addAttribute("searchValue",search.getSearchValue());
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("pageNum", criteria.getPage());

        return "/board/my-list";
    }

}
