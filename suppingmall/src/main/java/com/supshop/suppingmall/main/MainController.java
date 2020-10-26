package com.supshop.suppingmall.main;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.board.BoardService;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.common.Search;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.EightItemsCriteria;
import com.supshop.suppingmall.product.ProductSearch;
import com.supshop.suppingmall.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("")
@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final BoardService boardService;
    private final CategoryService categoryService;

    @RequestMapping("")
    public String mainPage(Model model){

        Criteria criteria = new EightItemsCriteria();
        model.addAttribute("latestComputerList",productService.getOnSaleProductsByParentCategoryOnMenu(4l,null,criteria));
        model.addAttribute("latestMobileList",productService.getOnSaleProductsByParentCategoryOnMenu(5l,null,criteria));



        Category it = categoryService.getCategoryByEnName("it-news");
        List<Board> newBoards = boardService.getBoardsByCategoryId(criteria, it.getId(), null);
        Category now = categoryService.getCategoryByEnName("now-how");
        List<Board> nowhowBoards = boardService.getBoardsByCategoryId(criteria, now.getId(), null);

        Category apple = categoryService.getCategoryByEnName("appleFarm");
        List<Board> appleBoards = boardService.getBoardsByCategoryId(criteria, apple.getId(), null);
        Category samsung = categoryService.getCategoryByEnName("samsungFarm");
        List<Board> samsungBoards = boardService.getBoardsByCategoryId(criteria, samsung.getId(), null);
        Category lg = categoryService.getCategoryByEnName("lgFarm");
        List<Board> lgBoards = boardService.getBoardsByCategoryId(criteria, lg.getId(), null);
        Category foreign = categoryService.getCategoryByEnName("foreignFarm");
        List<Board> foreignBoards = boardService.getBoardsByCategoryId(criteria, foreign.getId(), null);

        model.addAttribute("newBoards",newBoards);
        model.addAttribute("nowhowBoards",nowhowBoards);
        model.addAttribute("appleBoards",appleBoards);
        model.addAttribute("samsungBoards",samsungBoards);
        model.addAttribute("lgBoards",lgBoards);
        model.addAttribute("foreignBoards",foreignBoards);

        return "main.html";
    }

    @GetMapping("/error/403")
    public String error403(){
        return "/error/403";
    }

}
