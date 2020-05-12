package com.supshop.suppingmall.board;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.common.SessionUtils;
import com.supshop.suppingmall.page.BoardCriteria;
import com.supshop.suppingmall.page.BoardPageMaker;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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



    @GetMapping("/form")
    public String form(@RequestParam(required = false) Long categoryId,
                       Model model,
                       HttpSession session) {
        log.debug("'form'가 실행됨");
        UserVO user = SessionUtils.getSessionUser(session);
        if(user == null) {
            return "redirect:/users/loginform";
        }
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
    public String createBoard(Board board, HttpSession session) {
        log.debug("'createBoard'가 실행됨");
        UserVO user = SessionUtils.getSessionUser(session);

        if(user != null && user.getUserId().equals(board.getCreator().getUserId())) {
            boardService.createBoard(board);
            return "redirect:/boards";
        }
        return "redirect:/boards";
    }

    @GetMapping("/{id}/form")
    public String modifyBoard(@PathVariable Long id, Model model, HttpSession session) {
        log.debug("'modifyBoard'가 실행됨");
        Board board = boardService.getBoard(id);
        UserVO user = SessionUtils.getSessionUser(session);
        if(user == null || !user.getUserId().equals(board.getCreator().getUserId())) {
            return "redirect:/boards";
        }
        model.addAttribute("board",board);
        return "/board/updateform";
    }

    @PutMapping("/{id}")
    public String updateBoard(@PathVariable Long id, Board board, HttpSession session) {
        log.debug("'updateBoard'가 실행됨");
        UserVO user = SessionUtils.getSessionUser(session);
        board.setCreator(user);
        boardService.updateBoard(id, board);
        return "redirect:/boards/"+id;
    }

    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable Long id, HttpSession session) {
        log.debug("'deleteBoard'가 실행됨");
        UserVO user = SessionUtils.getSessionUser(session);
//        if(user.getUserId() != userId) {
//            return "redirect:/boards";
//        }
        boardService.deleteBoard(id);
        return "redirect:/boards";
    }

}
