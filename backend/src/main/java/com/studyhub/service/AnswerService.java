package com.studyhub.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyhub.entity.Answer;
import com.studyhub.mapper.AnswerMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService extends ServiceImpl<AnswerMapper, Answer> {
    
    public List<Answer> listByQuestion(Long questionId) {
        return lambdaQuery().eq(Answer::getQuestionId, questionId).list();
    }
}
