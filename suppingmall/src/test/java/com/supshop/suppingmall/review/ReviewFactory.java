package com.supshop.suppingmall.review;

import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductFactory;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.io.FileNotFoundException;

@Component
@ActiveProfiles("test")
public class ReviewFactory {

    @Autowired private UserFactory userFactory;
    @Autowired private ProductFactory productFactory;
    @Autowired private ReviewService reviewService;

    public Review buildReview(String userName) throws FileNotFoundException {
        User user = userFactory.createUser("user");
        User seller = userFactory.createSeller("seller");
        Product product = productFactory.createProduct("맥북", seller);
        Review review = Review.builder()
                .creator(user)
                .product(product)
                .title("너무 좋습니다.")
                .contents("너무 좋습니다. 사용하기에 너무 좋습니다.")
                .build();
        return review;
    }

    public Review createReview(String userName) throws FileNotFoundException {
        User user = userFactory.createUser(userName);
        User seller = userFactory.createSeller("seller");
        Product product = productFactory.createProduct("맥북", seller);
        Review review = Review.builder()
                .creator(user)
                .product(product)
                .title("너무 좋습니다.")
                .contents("너무 좋습니다. 사용하기에 너무 좋습니다.")
                .build();
        reviewService.createReview(review);
        return review;
    }


}
