package com.supshop.suppingmall.product;

import com.supshop.suppingmall.cart.Cart;
import com.supshop.suppingmall.cart.CartService;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.image.ImageController;
import com.supshop.suppingmall.image.ImageService;
import com.supshop.suppingmall.page.*;
import com.supshop.suppingmall.product.Form.ProductForm;
import com.supshop.suppingmall.product.Form.QnaForm;
import com.supshop.suppingmall.product.Form.QnaReplyForm;
import com.supshop.suppingmall.review.Review;
import com.supshop.suppingmall.review.ReviewService;
import com.supshop.suppingmall.user.Role;
import com.supshop.suppingmall.user.SessionUser;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserService;
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
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping("/products")
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final CartService cartService;
    private final QnAService qnaService;
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

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
        return "/product/list";
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


        return "/product/main";
    }

    //카테고리별 물품 조회
    @GetMapping("/category/{id}")
    public String getProductOnSaleInCategory(Model model,
                                       @AuthenticationPrincipal SessionUser user,
                                       @PathVariable Long id,
                                       ProductCriteria criteria) {


        int productsCount = productService.getProductsCount(id, null, null, Product.ProductStatus.SALE);

        model.addAttribute("count",productsCount);
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

        Criteria qnaCriteria = new TenItemsCriteria();
        List<QnA> qnaList = qnaService.getQnAList(qnaCriteria,id,null, null);
//        List<Board> reviews = boardService.getBoardsByProduct(id,reviewCategoryId);

        model.addAttribute("product",product);
        model.addAttribute("qnaList",qnaList);
//        model.addAttribute("reviews",reviews);

        return "/product/product";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProduct(id);

        Category category = categoryService.getGrandParentByGrandChildren(product.getCategory().getId());
        product.setCategory(category);

        Criteria qnaCriteria = new TenItemsCriteria();
        List<QnA> qnaList = qnaService.getQnAList(qnaCriteria,id,null, null);
        List<Review> reviewList = reviewService.getReviewList();

        model.addAttribute("product",product);
        model.addAttribute("qnaList",qnaList);
        model.addAttribute("reviews",reviewList);

        int qnaCount = qnaService.getQnACount(id, null, null);
        PageMaker qnaPageMaker = new PageMaker(qnaCount, 10, qnaCriteria);
        model.addAttribute("qnaPageMaker",qnaPageMaker);

        return "/product/product2";
    }


    @GetMapping("/seller")
    public String getProductsBySeller(@AuthenticationPrincipal SessionUser user,
                                      ThirtyItemsCriteria criteria,
                                      Model model) {

        int count = 0;
        List<Product> products = null;
        if(UserUtils.isSeller(user)){
            count = productService.getProductsCount(null, user.getUserId(),null,null);
            products = productService.getProductsBySeller(user.getUserId(),criteria);

        } else if(UserUtils.isAdmin(user)) {
            count = productService.getProductsCount();
            products = productService.getProducts(criteria);
        }

        PageMaker pageMaker = new PageMaker(count,productPagingCount,criteria);

        model.addAttribute("user",user);
        model.addAttribute("count",count);
        model.addAttribute("products",products);
        model.addAttribute("pageMaker",pageMaker);

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

    @GetMapping("/{productId}/qna/form")
    public String getQnaForm(@PathVariable Long productId,
                             Model model) {
        model.addAttribute("productId",productId);
        return "/product/qna/qnaForm";
    }

    @GetMapping("/{productId}/qna/{page}")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getQnaList(@PathVariable Long productId,
                                               @PathVariable int page,
                                               @RequestParam(required = false) String type,
                                               @RequestParam(required = false) String searchValue) {

        Map<String,Object> map = new HashMap<>();

        TenItemsCriteria criteria = new TenItemsCriteria();
        criteria.setPage(page);

        int qnACount = qnaService.getQnACount(productId, type, searchValue);
        PageMaker pageMaker = new PageMaker(qnACount,10,criteria);

        map.put("qnaPageMaker",pageMaker);

        List<QnA> qnAList = null;
        try {
            qnAList = qnaService.getQnAList(criteria, productId, type, searchValue);
            map.put("list",qnAList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(map);
    }

    @GetMapping("/qna/{qnaId}")
    @ResponseBody
    public ResponseEntity getQna(@PathVariable Long qnaId) {
        QnA qna = qnaService.getQna(qnaId);
        return ResponseEntity.ok(qna);
    }

    @PostMapping("/{productId}/qna")
    @ResponseBody
    public ResponseEntity createQnaByProductId(@RequestBody QnaForm qnaForm,
                                               @PathVariable Long productId,
                                               @AuthenticationPrincipal SessionUser sessionUser) {


        if(qnaForm.getUserId() != sessionUser.getUserId()) {
            return ResponseEntity.badRequest().body("유저 아이디가 일치하지 않습니다.");
        }

        User user = userService.getUser(qnaForm.getUserId());

        QnA qna = QnA.builder()
                        .title(qnaForm.getTitle())
                        .product(Product.builder().productId(productId).build())
                        .creator(user).build();

        qna = qnaService.createQnA(qna);
        URI uri = linkTo(ProductController.class).slash("/qna/"+qna.getQnaId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/{productId}/qna/{qnaId}/reply")
    @ResponseBody
    public ResponseEntity createReply(@RequestBody QnaReplyForm replyForm,
                                      @PathVariable Long productId,
                                      @PathVariable Long qnaId,
                                      @AuthenticationPrincipal SessionUser sessionUser) {

        Product product = productService.getProduct(productId);
        if(sessionUser.getUserId() != replyForm.getUserId() || (product.getSeller().getUserId() != replyForm.getUserId()))
            return ResponseEntity.badRequest().body("유저 아이디가 일치하지 않습니다.");

        User user = userService.getUser(replyForm.getUserId());
        QnA qna = qnaService.getQna(qnaId);

        QnA reply = QnA.builder()
                .title(replyForm.getTitle())
                .product(product)
                .parent(qna)
                .creator(user).build();

        qnaService.createReply(qnaId, reply);

        URI uri = linkTo(ProductController.class).slash("/qna/"+reply.getQnaId()).toUri();

        return ResponseEntity.created(uri).build();
    }


}
