package com.supshop.suppingmall.review;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ReviewServiceTest {

    @Autowired private ReviewService reviewService;
    @Autowired private ReviewFactory reviewFactory;

    @Test
    public void getReview() throws Exception{
        //given
        Review review = reviewFactory.createReview("user");
        //when
        Review searchReview = reviewService.getReview(review.getReviewId());

        //then
        assertEquals(review, searchReview);

    }

    @Test
    public void createReview() throws Exception {
        //given
        int count = reviewService.getReviewCount(null, null, null,null);
        Review review = reviewFactory.buildReview("user");
        //when
        reviewService.createReview(review);

        //then
        Assert.assertEquals(count+1, reviewService.getReviewCount(null, null, null,null));

    }

    @Test
    public void deleteReview() {
    }
}