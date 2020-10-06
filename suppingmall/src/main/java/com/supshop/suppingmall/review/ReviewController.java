package com.supshop.suppingmall.review;

import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.order.OrderItem;
import com.supshop.suppingmall.order.OrderItemService;
import com.supshop.suppingmall.order.Orders;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.page.TenItemsCriteria;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.review.form.ReviewForm;
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

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;
    private final OrderItemService orderItemService;
    private final ModelMapper modelMapper;


    @GetMapping("/form")
    public String getReviewForm(@RequestParam(required = false) Long orderItemId,
                                @AuthenticationPrincipal SessionUser sessionUser,
                                Model model){
        //해당 주문상품에 해당하는 구매자인지 or 구매확정이 된 주문상품인지 or 이미 작성된 리뷰가 있는지 확인
        System.out.println(orderItemId);
        OrderItem orderItem = orderItemService.getOrderItem(orderItemId);
//        if(!UserUtils.isOwner(orderItem.getBuyer().getUserId(), sessionUser) ||
//                !Orders.OrderStatus.COMPLETE.equals(orderItem.getStatus()) || "Y".equals(orderItem.getReviewYn())) {
//            return "";
//        }

        model.addAttribute("orderItem",orderItem);

        return "/review/form";
    }

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getReviewList(@RequestParam(required = false) Long productId,
                                                            @RequestParam(required = false) Long orderItemId,
                                                            @RequestParam(required = false) String type,
                                                            @RequestParam(required = false) String searchValue,
                                                            @RequestParam(required = false) int page){
        Map<String,Object> map = new HashMap<>();

        int reviewCount = reviewService.getReviewCount(productId,orderItemId,type,searchValue);

        Criteria criteria = new TenItemsCriteria();
        criteria.setPage(page);

        PageMaker pageMaker = new PageMaker(reviewCount, 10, criteria);

        map.put("reviewPageMaker", pageMaker);

        List<Review> reviewList = reviewService.getReviewList(criteria,productId,type,searchValue);
        map.put("list", reviewList);

        return ResponseEntity.ok(map);
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity createReview(@RequestBody ReviewForm form,
                                       @AuthenticationPrincipal SessionUser sessionUser){

        //요청자와 세션회원 아이디 확인
        if(form.getUserId() != sessionUser.getUserId())
            return ResponseEntity.badRequest().body("유저아이디가 일치하지 않습니다.");

        //실제 주문한 유저인지 확인 AND 해당 주문상품 번호로 이미 쓰여진 리뷰가 있는지 확인
        OrderItem orderItem = orderItemService.getOrderItem(form.getOrderItemId());

        //해당 주문상품에 해당하는 구매자인지 or 구매확정이 된 주문상품인지 or 이미 작성된 리뷰가 있는지 확인
                if(!UserUtils.isOwner(orderItem.getBuyer().getUserId(), sessionUser) ||
                !Orders.OrderStatus.COMPLETE.equals(orderItem.getStatus()) || "Y".equals(orderItem.getReviewYn())) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        User user = userService.getUser(form.getUserId());

        Review review = modelMapper.map(form, Review.class);
        review.setProduct(orderItem.getProduct());
        review.setCreator(user);

        reviewService.createReview(review);
        orderItem.setReviewYn("Y");
        orderItemService.updateOrderItem(orderItem);

        URI uri = linkTo(ReviewController.class).slash(review.getReviewId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Review> getReview(@PathVariable Long id){
        Review review = reviewService.getReview(id);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }


}
