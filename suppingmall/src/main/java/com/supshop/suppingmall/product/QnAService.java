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

    public int getQnACount(Long productId, String type, String searchValue) {
        return qnaMapper.findQnACount(productId, type, searchValue);
    }

    public List<QnA> getQnAList(Criteria criteria,Long productId, String type, String searchValue){
        return qnaMapper.findAll(criteria,productId,type,searchValue);
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
