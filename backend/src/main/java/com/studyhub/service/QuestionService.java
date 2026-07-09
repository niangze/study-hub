package com.studyhub.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyhub.dto.PageRequest;
import com.studyhub.dto.QuestionCreateRequest;
import com.studyhub.entity.Question;
import com.studyhub.entity.User;
import com.studyhub.mapper.QuestionMapper;
import com.studyhub.mapper.UserMapper;
import com.studyhub.vo.PageResult;
import com.studyhub.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService extends ServiceImpl<QuestionMapper, Question> {

    private final UserMapper userMapper;
    private final QuestionMapper questionMapper;

    @Transactional
    public QuestionVO createQuestion(Long userId, QuestionCreateRequest request) {
        Question question = new Question();
        BeanUtils.copyProperties(request, question);
        question.setUserId(userId);
        question.setStatus("OPEN");
        question.setAnswerCount(0);
        question.setViewCount(0);
        
        // Deduct points if reward > 0
        if (request.getPointsReward() != null && request.getPointsReward() > 0) {
            User user = userMapper.selectById(userId);
            if (user.getPoints() < request.getPointsReward()) {
                throw new RuntimeException("积分不足");
            }
            user.setPoints(user.getPoints() - request.getPointsReward());
            userMapper.updateById(user);
        }
        
        save(question);
        return convertToVO(question);
    }

    public PageResult<QuestionVO> listQuestions(PageRequest request) {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        
        // Keyword search
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            wrapper.and(w -> w.like(Question::getTitle, request.getKeyword())
                    .or()
                    .like(Question::getContent, request.getKeyword()));
        }
        
        // Category filter
        if (request.getCategoryId() != null) {
            wrapper.eq(Question::getCategoryId, request.getCategoryId());
        }
        
        // Sort
        if ("asc".equalsIgnoreCase(request.getOrder())) {
            wrapper.orderByAsc(Question::getCreatedAt);
        } else {
            wrapper.orderByDesc(Question::getCreatedAt);
        }
        
        Page<Question> page = new Page<>(request.getPage(), request.getSize());
        page(page, wrapper);
        
        List<QuestionVO> list = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return PageResult.of(list, page.getTotal(), request.getPage(), request.getSize());
    }

    public List<QuestionVO> listHotQuestions(int limit) {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Question::getViewCount)
               .orderByDesc(Question::getAnswerCount)
               .last("LIMIT " + limit);
        
        return list(wrapper).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    public List<QuestionVO> listLatestQuestions(int limit) {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Question::getCreatedAt)
               .last("LIMIT " + limit);
        
        return list(wrapper).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    public QuestionVO getQuestionDetail(Long id) {
        Question question = getById(id);
        if (question == null) {
            throw new RuntimeException("问题不存在");
        }
        
        // Increment view count
        question.setViewCount(question.getViewCount() + 1);
        updateById(question);
        
        return convertToVO(question);
    }

    @Transactional
    public void deleteQuestion(Long id, Long userId) {
        Question question = getById(id);
        if (question == null) {
            throw new RuntimeException("问题不存在");
        }
        
        // Only author or admin can delete
        if (!question.getUserId().equals(userId)) {
            User user = userMapper.selectById(userId);
            if (!"ADMIN".equals(user.getRole())) {
                throw new RuntimeException("无权删除");
            }
        }
        
        removeById(id);
    }

    @Transactional
    public void closeQuestion(Long id, Long userId) {
        Question question = getById(id);
        if (question == null) {
            throw new RuntimeException("问题不存在");
        }
        
        if (!question.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }
        
        question.setStatus("CLOSED");
        updateById(question);
    }

    public Long countByStatus(String status) {
        if (status == null) {
            return count();
        }
        return lambdaQuery().eq(Question::getStatus, status).count();
    }

    private QuestionVO convertToVO(Question question) {
        QuestionVO vo = new QuestionVO();
        BeanUtils.copyProperties(question, vo);
        
        // Get username
        User user = userMapper.selectById(question.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setUserAvatar(user.getAvatar());
        }
        
        // Get category name
        // TODO: join category table
        
        return vo;
    }
}
