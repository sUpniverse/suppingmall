package com.supshop.suppingmall.main;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.board.BoardService;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.EightItemsCriteria;
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

        Criteria productCriteria = new EightItemsCriteria();
        model.addAttribute("latestComputerList",productService.getOnSaleProductsByParentCategoryOnMenu(4l,productCriteria));
        model.addAttribute("latestMobileList",productService.getOnSaleProductsByParentCategoryOnMenu(5l,productCriteria));



        Category it = categoryService.getCategoryByEnName("it-news");
        List<Board> newBoards = boardService.getBoardsByCategoryId(productCriteria, it.getId(), null, null);
        Category now = categoryService.getCategoryByEnName("now-how");
        List<Board> nowhowBoards = boardService.getBoardsByCategoryId(productCriteria, now.getId(), null, null);

        Category apple = categoryService.getCategoryByEnName("appleFarm");
        List<Board> appleBoards = boardService.getBoardsByCategoryId(productCriteria, apple.getId(), null, null);
        Category samsung = categoryService.getCategoryByEnName("samsungFarm");
        List<Board> samsungBoards = boardService.getBoardsByCategoryId(productCriteria, samsung.getId(), null, null);
        Category lg = categoryService.getCategoryByEnName("lgFarm");
        List<Board> lgBoards = boardService.getBoardsByCategoryId(productCriteria, lg.getId(), null, null);
        Category foreign = categoryService.getCategoryByEnName("foreignFarm");
        List<Board> foreignBoards = boardService.getBoardsByCategoryId(productCriteria, foreign.getId(), null, null);

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
