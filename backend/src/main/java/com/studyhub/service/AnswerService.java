package com.studyhub.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyhub.entity.Answer;
import com.studyhub.entity.Question;
import com.studyhub.entity.User;
import com.studyhub.mapper.AnswerMapper;
import com.studyhub.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService extends ServiceImpl<AnswerMapper, Answer> {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionMapper questionMapper;

    public List<Answer> listByQuestion(Long questionId) {
        return lambdaQuery().eq(Answer::getQuestionId, questionId).list();
    }

    @Transactional
    public Answer submit(Long userId, Long questionId, String content) {
        Question q = questionService.getById(questionId);
        if (q == null) throw new RuntimeException("问题不存在");
        if (!"OPEN".equals(q.getStatus())) throw new RuntimeException("该问题已关闭或已解决");
        if (q.getUserId().equals(userId)) throw new RuntimeException("不能回答自己的问题");
        Answer a = new Answer();
        a.setUserId(userId);
        a.setQuestionId(questionId);
        a.setContent(content);
        a.setIsAccepted(false);
        save(a);
        questionMapper.incrementAnswerCount(questionId);
        return a;
    }

    @Transactional
    public Answer updateAnswer(Long answerId, Long userId, String content) {
        Answer a = getById(answerId);
        if (a == null) throw new RuntimeException("回答不存在");
        if (!a.getUserId().equals(userId)) throw new RuntimeException("只能编辑自己的回答");
        if (Boolean.TRUE.equals(a.getIsAccepted())) throw new RuntimeException("已被采纳的回答不可编辑");
        a.setContent(content);
        updateById(a);
        return a;
    }

    @Transactional
    public void deleteAnswer(Long answerId, Long userId) {
        Answer a = getById(answerId);
        if (a == null) throw new RuntimeException("回答不存在");
        if (!a.getUserId().equals(userId)) throw new RuntimeException("只能删除自己的回答");
        if (Boolean.TRUE.equals(a.getIsAccepted())) throw new RuntimeException("已被采纳的回答不可删除");
        removeById(answerId);
        questionMapper.decrementAnswerCount(a.getQuestionId());
    }

    @Transactional
    public void acceptAnswer(Long answerId, Long userId) {
        Answer a = getById(answerId);
        if (a == null) throw new RuntimeException("回答不存在");
        Question q = questionService.getById(a.getQuestionId());
        if (q == null) throw new RuntimeException("问题不存在");
        if (!q.getUserId().equals(userId)) throw new RuntimeException("只有提问者可以采纳");
        if (!"OPEN".equals(q.getStatus())) throw new RuntimeException("该问题已处理");
        long count = lambdaQuery().eq(Answer::getQuestionId, q.getId()).eq(Answer::getIsAccepted, true).count();
        if (count > 0) throw new RuntimeException("已采纳过最佳答案");
        a.setIsAccepted(true);
        updateById(a);
        q.setStatus("RESOLVED");
        questionService.updateById(q);
        User answerUser = userService.getById(a.getUserId());
        if (answerUser != null) {
            int reward = (q.getPointsReward() != null ? q.getPointsReward() : 0) + 10;
            int current = answerUser.getPoints() != null ? answerUser.getPoints() : 0;
            answerUser.setPoints(current + reward);
            userService.updateById(answerUser);
        }
    }

    public List<Answer> myAnswers(Long userId) {
        return lambdaQuery().eq(Answer::getUserId, userId).orderByDesc(Answer::getCreatedAt).list();
    }
}
