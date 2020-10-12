package com.supshop.suppingmall.product;

import com.supshop.suppingmall.mapper.QnAMapper;
import com.supshop.suppingmall.page.Criteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnAService {

    private final QnAMapper qnaMapper;

    public int getQnACount(String type, String searchValue) {
        return qnaMapper.findCount(type, searchValue);
    }
    public int getQnACountByProductId(Long productId, String type, String searchValue) {
        return qnaMapper.findCountByProductId(productId, type, searchValue);
    }
    public int getQnACountByUserId(Long userId, String type, String searchValue) {
        return qnaMapper.findCountByUserId(userId, type, searchValue);
    }

    public List<QnA> getQnAList(Criteria criteria, String type, String searchValue){
        return qnaMapper.findAll(criteria,type,searchValue);
    }
    public List<QnA> getQnAListByProductId(Criteria criteria, Long productId, String type, String searchValue){
        return qnaMapper.findByProductId(criteria,productId,type,searchValue);
    }
    public List<QnA> getQnAListByUserId(Criteria criteria, Long userId, String type, String searchValue){
        return qnaMapper.findByUserId(criteria,userId,type,searchValue);
    }

    public QnA getQna(Long id) {
        return qnaMapper.findOne(id);
    }

    @Transactional
    public QnA createQnA(QnA qna) {
        qnaMapper.save(qna);
        return qna;
    }

    @Transactional
    protected void updateQna(QnA qna){
        qnaMapper.update(qna);
    }

    @Transactional
    public QnA createReply(Long qnaId, QnA reply) {
        reply = createQnA(reply);
        QnA qna = getQna(qnaId);
        qna.setReply(reply);
        updateQna(qna);
        return reply;
    }
}
