package com.supshop.suppingmall.main;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.board.BoardService;
import com.supshop.suppingmall.page.BoardCriteria;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.ProductCriteria;
import com.supshop.suppingmall.product.Product;
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

        Criteria productCriteria = new ProductCriteria();
        model.addAttribute("latestComputerList",productService.getOnSaleProductsByParentCategoryOnMenu(4l,productCriteria));
        model.addAttribute("latestMobileList",productService.getOnSaleProductsByParentCategoryOnMenu(5l,productCriteria));


//        List<Board> newBoards = boardService.getBoardByCondition(productCriteria, categoryId, null, null);
//        List<Board> storyBoards = boardService.getBoardByCondition(productCriteria, categoryId, null, null);
        List<Board> appleBoards = boardService.getBoardByCondition(productCriteria, 24l, null, null);
        List<Board> samsungBoards = boardService.getBoardByCondition(productCriteria, 25l, null, null);
        List<Board> lgBoards = boardService.getBoardByCondition(productCriteria, 26l, null, null);
        List<Board> foreignBoards = boardService.getBoardByCondition(productCriteria, 27l, null, null);

        model.addAttribute("appleBoards",appleBoards);
        model.addAttribute("samsungBoards",samsungBoards);
        model.addAttribute("lgBoards",lgBoards);
        model.addAttribute("foreignBoards",foreignBoards);

        return "main.html";
    }

    @RequestMapping("/test")
    public String testPage(){
        return "test.html";
    }
}
