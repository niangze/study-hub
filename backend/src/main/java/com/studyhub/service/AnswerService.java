package com.studyhub.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyhub.dto.AnswerCreateRequest;
import com.studyhub.entity.Answer;
import com.studyhub.entity.Question;
import com.studyhub.entity.User;
import com.studyhub.mapper.AnswerMapper;
import com.studyhub.mapper.QuestionMapper;
import com.studyhub.mapper.UserMapper;
import com.studyhub.vo.AnswerVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService extends ServiceImpl<AnswerMapper, Answer> {

    private final QuestionMapper questionMapper;
    private final UserMapper userMapper;

    @Transactional
    public AnswerVO createAnswer(Long userId, AnswerCreateRequest request) {
        // Check question exists and is open
        Question question = questionMapper.selectById(request.getQuestionId());
        if (question == null) {
            throw new RuntimeException("问题不存在");
        }
        if ("CLOSED".equals(question.getStatus())) {
            throw new RuntimeException("问题已关闭，无法回答");
        }
        
        Answer answer = new Answer();
        answer.setContent(request.getContent());
        answer.setQuestionId(request.getQuestionId());
        answer.setUserId(userId);
        answer.setIsAccepted(false);
        
        save(answer);
        
        // Update question answer count
        question.setAnswerCount(question.getAnswerCount() + 1);
        questionMapper.updateById(question);
        
        return convertToVO(answer);
    }

    public List<AnswerVO> listAnswersByQuestion(Long questionId) {
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Answer::getQuestionId, questionId)
               .orderByDesc(Answer::getCreatedAt);
        
        return list(wrapper).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void acceptAnswer(Long answerId, Long userId) {
        Answer answer = getById(answerId);
        if (answer == null) {
            throw new RuntimeException("回答不存在");
        }
        
        Question question = questionMapper.selectById(answer.getQuestionId());
        if (question == null) {
            throw new RuntimeException("问题不存在");
        }
        
        // Only question author can accept
        if (!question.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }
        
        // Unaccept previous answer
        lambdaUpdate()
                .eq(Answer::getQuestionId, answer.getQuestionId())
                .set(Answer::getIsAccepted, false)
                .update();
        
        // Accept this answer
        answer.setIsAccepted(true);
        updateById(answer);
        
        // Close question and reward points
        question.setStatus("CLOSED");
        questionMapper.updateById(question);
        
        // Reward points to answer author
        if (question.getPointsReward() != null && question.getPointsReward() > 0) {
            User answerAuthor = userMapper.selectById(answer.getUserId());
            if (answerAuthor != null) {
                answerAuthor.setPoints(answerAuthor.getPoints() + question.getPointsReward());
                userMapper.updateById(answerAuthor);
            }
        }
    }

    @Transactional
    public void deleteAnswer(Long answerId, Long userId) {
        Answer answer = getById(answerId);
        if (answer == null) {
            throw new RuntimeException("回答不存在");
        }
        
        if (!answer.getUserId().equals(userId)) {
            User user = userMapper.selectById(userId);
            if (!"ADMIN".equals(user.getRole())) {
                throw new RuntimeException("无权删除");
            }
        }
        
        removeById(answerId);
        
        // Update question answer count
        Question question = questionMapper.selectById(answer.getQuestionId());
        if (question != null) {
            question.setAnswerCount(Math.max(0, question.getAnswerCount() - 1));
            questionMapper.updateById(question);
        }
    }

    public Long countAnswers() {
        return count();
    }

    private AnswerVO convertToVO(Answer answer) {
        AnswerVO vo = new AnswerVO();
        BeanUtils.copyProperties(answer, vo);
        
        User user = userMapper.selectById(answer.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setUserAvatar(user.getAvatar());
        }
        
        return vo;
    }
}
