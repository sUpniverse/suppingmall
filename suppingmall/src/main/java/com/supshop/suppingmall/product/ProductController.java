package com.supshop.suppingmall.product;

import com.supshop.suppingmall.board.Board;
import com.supshop.suppingmall.board.BoardService;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.common.SessionUtils;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.image.ImageController;
import com.supshop.suppingmall.image.ImageService;
import com.supshop.suppingmall.product.Form.ProductForm;
import com.supshop.suppingmall.product.Form.QnaForm;
import com.supshop.suppingmall.user.Role;
import com.supshop.suppingmall.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
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
    private final ModelMapper modelMapper;

    private static final Long qnaCategoryId = 30l;
    private static final Long reviewCategoryId = 29l;
    private static final Long productCategoryId = 2L;

    @GetMapping("/form")
    public String form(HttpSession session,Model model) {
        UserVO user = (UserVO) session.getAttribute("user");
        if(user == null || !(user.getRole().equals(Role.SELLER) || user.getRole().equals(Role.MASTER))) {
            return "redirect:/users/loginform";
        }
        model.addAttribute("categories",categoryService.getCategory(productCategoryId).getChild());
        model.addAttribute("vendors", Delivery.DeliveryVendor.values());
        return "/product/form";
    }

    @GetMapping("")
    public String getAllProduct(Model model) {
        List<Product> products = productService.retrieveAllProduct();
        model.addAttribute("products",products);
        return "/product/list";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Product product = productService.findProduct(id);
        List<Board> qnaList = boardService.getBoardsByProduct(id,qnaCategoryId);
        List<Board> reviews = boardService.getBoardsByProduct(id,reviewCategoryId);

        model.addAttribute("product",product);
        model.addAttribute("qnaList",qnaList);
        model.addAttribute("reviews",reviews);
        return "/product/product";
    }

    @PostMapping("")
    public String createProduct(@Valid ProductForm productForm, MultipartFile[] thumnails, HttpSession session) {
        UserVO user = (UserVO) session.getAttribute("user");
        if(user == null || !(user.getRole().equals(Role.SELLER) || user.getRole().equals(Role.MASTER))) {
            return "redirect:/users/loginform";
        }
        Product product = modelMapper.map(productForm, Product.class);
        String thumnail = null;
        if(thumnails != null) {
            for(MultipartFile file : thumnails) {
                try {
                    thumnail = imageService.saveImage(file, ImageController.productSourceUrl,ImageController.productUri).toString();
                } catch (IOException e) {

                }
            }
            product.setThumbnail(thumnail);
        }
        productService.createProduct(product);
        return "forward:/";
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable Long id, Product product) {
        return "";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        return "";
    }

    @GetMapping("/seller")
    public String getProductsBySeller(HttpSession session,Model model) {
        UserVO sessionUser = SessionUtils.getSessionUser(session);
        List<Product> products = productService.findProductsBySellerId(sessionUser.getUserId());
        model.addAttribute("user",sessionUser);
        model.addAttribute("products",products);
        return "/product/seller/list";
    }

    @GetMapping("/{productId}/qnas/form")
    public String getQnaForm(@PathVariable Long productId,
                             HttpSession session,
                             Model model) {
        UserVO sessionUser = SessionUtils.getSessionUser(session);
        if(SessionUtils.isSessionNull(session)) {
            return "redirect:/users/loginform";
        }
        model.addAttribute("productId",productId);
        return "/product/board/qnaForm";
    }

    @PostMapping("/{id}/qnas")
    @ResponseBody
    public ResponseEntity createQnaByProductId(@RequestBody QnaForm qna,
                                               @PathVariable Long id,
                                               HttpSession session) {

        if(SessionUtils.isSessionNull(session)) {
            return ResponseEntity.badRequest().build();
        }
                Board board = Board.builder()
                .category(Category.builder().id(30l).build())
                .title(qna.getTitle())
                .product(Product.builder().productId(id).build())
                .creator(UserVO.builder().userId(qna.getUserId()).build()).build();
        boardService.createBoard(board);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/qnas/{id}")
    @ResponseBody
    public ResponseEntity getQna(@PathVariable Long id) {
        Board board = boardService.getBoardByProduct(id);
        return ResponseEntity.ok(board);
    }

    @GetMapping("/qnas/{qnaId}/updateForm")
    public String getQnaUpdateForm(@PathVariable Long qnaId,
                             HttpSession session,
                             Model model) {
        Board board = boardService.getBoard(qnaId);
        if(!SessionUtils.isSameUser(board.getCreator().getUserId(),session)) {
            return "redirect:/users/loginform";
        }
        model.addAttribute("qna",board);
        return "/product/board/editQnaForm";
    }

    @PutMapping("/qnas/{qnaId}")
    @ResponseBody
    public ResponseEntity updateQna(@PathVariable Long qnaId,
                                    @RequestBody QnaForm qna,
                                    HttpSession session) {
        Board oldQnA = boardService.getBoard(qnaId);
        if(!SessionUtils.isSameUser(oldQnA.getCreator().getUserId(),session)) {
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
                                       HttpSession session) {

        Board oldQnA = boardService.getBoard(qnaId);
        if(!SessionUtils.isSameUser(oldQnA.getCreator().getUserId(),session)) {
            return ResponseEntity.badRequest().build();
        }

        boardService.deleteBoard(qnaId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{productId}/reivews")
    public String getReviewForm(@PathVariable Long productId) {
        return "";
    }
}
