package com.supshop.suppingmall.product;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.board.BoardService;
import com.supshop.suppingmall.cart.Cart;
import com.supshop.suppingmall.cart.CartService;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.image.ImageController;
import com.supshop.suppingmall.image.ImageService;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.page.ProductCriteria;
import com.supshop.suppingmall.product.Form.ProductForm;
import com.supshop.suppingmall.product.Form.QnaForm;
import com.supshop.suppingmall.user.SessionUser;
import com.supshop.suppingmall.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequestMapping("/products")
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final BoardService boardService;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    private static final Long qnaCategoryId = 30l;
    private static final Long reviewCategoryId = 29l;
    private static final Long productCategoryId = 2L;
    private static final Long electronicsCategoryId = 3L;
    private static final Long clothingCategoryId = 7L;

    private static final int productPagingCount = 5;


    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("categories",categoryService.getCategory(productCategoryId).getChild());
        model.addAttribute("vendors", Delivery.DeliveryVendor.values());
        return "/product/form";
    }

    //상태에 관계없이 모든 상품
    @GetMapping("")
    public String getAllProduct(Model model) {
        List<Product> products = productService.getProducts();
        model.addAttribute("products",products);
        model.addAttribute("categories",categoryService.getCategoryToGrandChildren(productCategoryId));
        return "/product/list";
    }

    //판매상태의 모든 물품들
    @GetMapping("/main2")
    public String getAllOnSaleProduct2(Model model,
                                      @RequestParam(required = false)String name) {
        List<Product> products = productService.getOnSaleProductsOnMenu(null,name,null);
        model.addAttribute("products",products);
        model.addAttribute("categories",categoryService.getCategoryToGrandChildren(productCategoryId).getChild());
        return "/product/list2";
    }

    //판매상태의 모든 물품들
    @GetMapping("/main")
    public String getAllOnSaleProduct(Model model,
                                       @AuthenticationPrincipal SessionUser user,
                                       @RequestParam(required = false)String name) {

        List<Cart> cart = null;
        if(user != null) cart = cartService.findCartByBuyerId(user.getUserId());

        if(cart != null) model.addAttribute("cart",cart.size());

        model.addAttribute("electronics",categoryService.getCategoryToGrandChildren(electronicsCategoryId));
        model.addAttribute("clothing",categoryService.getCategoryToGrandChildren(clothingCategoryId));

        Criteria criteria = new ProductCriteria();
        model.addAttribute("latestComputerList",productService.getOnSaleProductsByParentCategoryOnMenu(4l,criteria));
        model.addAttribute("latestMobileList",productService.getOnSaleProductsByParentCategoryOnMenu(5l,criteria));
        model.addAttribute("productList",productService.getOnSaleProductsOnMenu(null, null, criteria));


        return "/product/list";
    }

    //카테고리별 물품 조회
    @GetMapping("/category/{id}")
    public String getProductOnSaleInCategory(Model model,
                                       @AuthenticationPrincipal SessionUser user,
                                       @PathVariable Long id,
                                       ProductCriteria criteria) {


        int productsCount = productService.getProductsCount(id, null, null, null);

        model.addAttribute("productList",productService.getOnSaleProductsOnMenu(id, null, criteria));
        model.addAttribute("categoryId",id);
        model.addAttribute("productPageMaker",new PageMaker(productsCount, productPagingCount, criteria));


        return "/product/list-category";
    }


    @GetMapping("/{id}/2")
    public String getProduct2(@PathVariable Long id, Model model) {
        Product product = productService.getProduct(id);

        Category category = categoryService.getGrandParentByGrandChildren(product.getCategory().getId());
        product.setCategory(category);

        List<Board> qnaList = boardService.getBoardsByProduct(id,qnaCategoryId);
        List<Board> reviews = boardService.getBoardsByProduct(id,reviewCategoryId);

        model.addAttribute("product",product);
        model.addAttribute("qnaList",qnaList);
        model.addAttribute("reviews",reviews);
        return "/product/product";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProduct(id);

        Category category = categoryService.getGrandParentByGrandChildren(product.getCategory().getId());
        product.setCategory(category);

        List<Board> qnaList = boardService.getBoardsByProduct(id,qnaCategoryId);
        List<Board> reviews = boardService.getBoardsByProduct(id,reviewCategoryId);

        model.addAttribute("product",product);
        model.addAttribute("qnaList",qnaList);
        model.addAttribute("reviews",reviews);
        return "/product/product2";
    }


    @GetMapping("/seller")
    public String getProductsBySeller(@AuthenticationPrincipal SessionUser user,
                                      ProductCriteria productCriteria,
                                      Model model) {

        int count = 0;
        List<Product> products = null;
        if(UserUtils.isSeller(user)){
            count = productService.getProductsCount(null, user.getUserId(),null,null);
            products = productService.getProductsBySeller(user.getUserId(),productCriteria);

        } else if(UserUtils.isAdmin(user)) {
            count = productService.getProductsCount(null, null,null,null);
            products = productService.getProducts(productCriteria);
        }

        //option의 문제로 1개의 제품이 아닌 같은 ID로 여러개의 제품이 뜨므로 페이징 불가
//        PageMaker pageMaker = new PageMaker(count,productPagingCount,productCriteria);



        model.addAttribute("user",user);
        model.addAttribute("count",count);
        model.addAttribute("products",products);
//        model.addAttribute("pageMaker",pageMaker);

        return "/product/seller/list";
    }

    @PostMapping("")
    public String createProduct(@Valid ProductForm productForm,
                                MultipartFile[] thumnails,
                                @AuthenticationPrincipal SessionUser sessionUser) {
        Product product = modelMapper.map(productForm, Product.class);
        String thumnail = null;
        if(thumnails.length > 0) {
            for(MultipartFile file : thumnails) {
                try {
                    thumnail = imageService.saveImage(file, ImageController.productSourceUrl,ImageController.productUri,sessionUser.getUserId()).toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    //Todo : 이미지 저장 실패시 다시 시도 or 게시글 저장 실패
                }
            }
            product.setThumbnail(thumnail);
        }
        productService.createProduct(product,productForm.getImagesUrl());
        return "redirect:/products/"+product.getProductId();
    }

    @PatchMapping("/{id}/status/{status}")
    @ResponseBody
    public ResponseEntity updateProductStatus(@PathVariable Long id, @PathVariable Product.ProductStatus status) {
        Product product = productService.getProduct(id);
        product.setStatus(status);
        try {
            productService.updateProduct(id, product);
        } catch (Exception e){
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable Long id, Product product) {
        return "";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        return "";
    }

    @GetMapping("/{productId}/qnas/form")
    public String getQnaForm(@PathVariable Long productId,
                             Model model) {
        model.addAttribute("productId",productId);
        return "/product/board/qnaForm";
    }

    @PostMapping("/{productId}/qnas")
    @ResponseBody
    public ResponseEntity createQnaByProductId(@RequestBody QnaForm qna,
                                               @PathVariable Long productId) {

        Board board = Board.builder()
                        .category(Category.builder().id(30l).build())
                        .title(qna.getTitle())
                        .product(Product.builder().productId(productId).build())
                        .creator(User.builder().userId(qna.getUserId()).build()).build();
        boardService.createBoard(board,null);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/qnas/{qnaId}")
    @ResponseBody
    public ResponseEntity getQna(@PathVariable Long qnaId) {
        Board board = boardService.getBoardByProduct(qnaId);
        return ResponseEntity.ok(board);
    }

    @GetMapping("/qnas/{qnaId}/updateForm")
    public String getQnaUpdateForm(@PathVariable Long qnaId,
                             @AuthenticationPrincipal SessionUser user,
                             Model model) {
        Board board = boardService.getBoard(qnaId);
        if(!UserUtils.isOwner(board.getCreator().getUserId(),user)) {
            return "redirect:/users/loginform";
        }
        model.addAttribute("qna",board);
        return "/product/board/editQnaForm";
    }

    @PutMapping("/qnas/{qnaId}")
    @ResponseBody
    public ResponseEntity updateQna(@PathVariable Long qnaId,
                                    @RequestBody QnaForm qna,
                                    @AuthenticationPrincipal SessionUser user) {
        Board oldQnA = boardService.getBoard(qnaId);
        if(!UserUtils.isOwner(oldQnA.getCreator().getUserId(),user)) {
            return ResponseEntity.badRequest().build();
        }
        Board board = Board.builder()
                .title(qna.getTitle()).build();
        boardService.updateBoard(qnaId, board);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/qnas/{qnaId}")
    @ResponseBody
    public ResponseEntity deleteQna(@PathVariable Long qnaId,
                                    @AuthenticationPrincipal SessionUser user) {

        Board oldQnA = boardService.getBoard(qnaId);
        if(!UserUtils.isOwner(oldQnA.getCreator().getUserId(),user)) {
            return ResponseEntity.badRequest().build();
        }

        boardService.deleteBoard(qnaId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{productId}/reivews/form")
    public String getReviewForm(@PathVariable Long productId) {
        return "";
    }
}
