package com.supshop.suppingmall.main;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.board.BoardService;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.EightItemsCriteria;
import com.supshop.suppingmall.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("")
@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final BoardService boardService;

    @RequestMapping("")
    public String mainPage(Model model){

        Criteria productCriteria = new EightItemsCriteria();
        model.addAttribute("latestComputerList",productService.getOnSaleProductsByParentCategoryOnMenu(4l,productCriteria));
        model.addAttribute("latestMobileList",productService.getOnSaleProductsByParentCategoryOnMenu(5l,productCriteria));


        List<Board> newBoards = boardService.getBoardsByCategoryId(productCriteria, 91l, null, null);
//        List<Board> storyBoards = boardService.getBoardByCondition(productCriteria, categoryId, null, null);

        List<Board> appleBoards = boardService.getBoardsByCategoryId(productCriteria, 24l, null, null);
        List<Board> samsungBoards = boardService.getBoardsByCategoryId(productCriteria, 25l, null, null);
        List<Board> lgBoards = boardService.getBoardsByCategoryId(productCriteria, 26l, null, null);
        List<Board> foreignBoards = boardService.getBoardsByCategoryId(productCriteria, 27l, null, null);

        model.addAttribute("newBoards",newBoards);
        model.addAttribute("appleBoards",appleBoards);
        model.addAttribute("samsungBoards",samsungBoards);
        model.addAttribute("lgBoards",lgBoards);
        model.addAttribute("foreignBoards",foreignBoards);

        return "main.html";
    }

}
