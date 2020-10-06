package com.supshop.suppingmall.review;

import com.supshop.suppingmall.mapper.ReviewMapper;
import com.supshop.suppingmall.page.Criteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;

    public List<Review> getReviewList(Criteria criteria, Long productId, String type, String searchValue) {
        List<Review> reviewList = reviewMapper.findAll(criteria, productId, null, type, searchValue);
        return reviewList;
    }

    public Review getReview(Long id) {
        return reviewMapper.findOne(id);
    }

    @Transactional
    public Review createReview(Review review) {
        reviewMapper.save(review);
        return review;
    }

    public int getReviewCount(Long productId, Long orderItemId, String type, String searchValue) {
        return reviewMapper.findReviewCount(productId,orderItemId, type, searchValue);
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewMapper.delete(id);
    }
}
