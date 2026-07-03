package com.studyhub.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyhub.entity.Answer;
import com.studyhub.entity.Question;
import com.studyhub.mapper.AnswerMapper;
import com.studyhub.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService extends ServiceImpl<QuestionMapper, Question> {
    
    @Autowired
    private AnswerMapper answerMapper;
    
    public List<Question> listByCategory(Long categoryId) {
        return lambdaQuery().eq(Question::getCategoryId, categoryId).list();
    }
    
    public List<Question> search(String keyword) {
        return lambdaQuery().like(Question::getTitle, keyword).list();
    }
    
    public void acceptAnswer(Long answerId) {
        Answer answer = answerMapper.selectById(answerId);
        if (answer != null) {
            answer.setIsAccepted(true);
            answerMapper.updateById(answer);
            // 更新问题状态
            Question question = getById(answer.getQuestionId());
            question.setStatus("CLOSED");
            updateById(question);
        }
    }
}
