package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.product.QnA;

import java.util.List;

public interface QnAMapper {

    // paging을 위한 조건에 따른 갯수
    int findCount(String type, String searchValue);
    int findCountByProductId(Long productId, String type, String searchValue);
    int findCountByUserId(Long userId, String type, String searchValue);
    int findCountBySellerId(Long sellerId, String type, String searchValue);

    List<QnA> findAll(Criteria criteria,String type, String searchValue);
    List<QnA> findByProductId(Criteria criteria, Long productId, String type, String searchValue);
    List<QnA> findByUserId(Criteria criteria, Long userId, String type, String searchValue);
    List<QnA> findBySellerId(Criteria criteria, Long sellerId, String type, String searchValue);

    QnA findOne(Long qnaId);

    void save(QnA qna);

    void update(QnA qna);

    void delete(Long qnaId);
}
