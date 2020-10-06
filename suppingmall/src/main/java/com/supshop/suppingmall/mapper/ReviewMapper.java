package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.product.QnA;
import com.supshop.suppingmall.review.Review;

import java.util.List;

public interface ReviewMapper {

    // paging을 위한 조건에 따른 갯수
    int findReviewCount(Long productId,Long orderItemId, String type, String searchValue);

    List<Review> findAll(Criteria criteria, Long productId,Long orderItemId, String type, String searchValue);

    Review findOne(Long reviewId);

    void save(Review review);

    void update(Review review);

    void delete(Long reviewId);
}
