package com.studyhub.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyhub.entity.Question;
import com.studyhub.entity.User;
import com.studyhub.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService extends ServiceImpl<QuestionMapper, Question> {

    @Autowired
    private UserService userService;

    public List<Question> listByCategory(Long categoryId) {
        return lambdaQuery().eq(Question::getCategoryId, categoryId).list();
    }

    public List<Question> search(String keyword) {
        return lambdaQuery().like(Question::getTitle, keyword).list();
    }

    @Transactional
    public Question create(Long userId, String title, String content, Long categoryId, Integer pointsReward) {
        if (pointsReward != null && pointsReward > 0) {
            User user = userService.getById(userId);
            if (user == null) throw new RuntimeException("用户不存在");
            if (user.getPoints() == null || user.getPoints() < pointsReward)
                throw new RuntimeException("积分不足");
            user.setPoints(user.getPoints() - pointsReward);
            userService.updateById(user);
        }
        Question q = new Question();
        q.setUserId(userId);
        q.setTitle(title);
        q.setContent(content);
        q.setCategoryId(categoryId);
        q.setPointsReward(pointsReward != null ? pointsReward : 0);
        q.setStatus("OPEN");
        save(q);
        return q;
    }

    @Transactional
    public Question updateQuestion(Long questionId, Long userId, String title, String content, Long categoryId) {
        Question q = getById(questionId);
        if (q == null) throw new RuntimeException("问题不存在");
        if (!q.getUserId().equals(userId)) throw new RuntimeException("只能编辑自己的问题");
        if (!"OPEN".equals(q.getStatus())) throw new RuntimeException("只能编辑待回答状态的问题");
        q.setTitle(title);
        q.setContent(content);
        q.setCategoryId(categoryId);
        updateById(q);
        return q;
    }

    @Transactional
    public void deleteQuestion(Long questionId, Long userId) {
        Question q = getById(questionId);
        if (q == null) throw new RuntimeException("问题不存在");
        if (!q.getUserId().equals(userId)) throw new RuntimeException("只能删除自己的问题");
        int reward = q.getPointsReward() != null ? q.getPointsReward() : 0;
        if ("OPEN".equals(q.getStatus()) && reward > 0) {
            User user = userService.getById(userId);
            if (user != null) {
                int currentPoints = user.getPoints() != null ? user.getPoints() : 0;
                user.setPoints(currentPoints + reward);
                userService.updateById(user);
            }
        }
        removeById(questionId);
    }

    public Question getDetail(Long questionId) {
        Question q = getById(questionId);
        if (q == null) throw new RuntimeException("问题不存在");
        baseMapper.incrementViewCount(questionId);
        q.setViewCount(q.getViewCount() != null ? q.getViewCount() + 1 : 1);
        return q;
    }

    public Page<Question> list(Integer page, Integer size, String keyword, Long categoryId, String sortBy, String status) {
        LambdaQueryWrapper<Question> w = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty())
            w.and(wr -> wr.like(Question::getTitle, keyword).or().like(Question::getContent, keyword));
        if (categoryId != null) w.eq(Question::getCategoryId, categoryId);
        if (status != null && !status.isEmpty()) w.eq(Question::getStatus, status);
        if ("unresolved".equals(sortBy)) {
            w.eq(Question::getStatus, "OPEN").orderByDesc(Question::getCreatedAt);
        } else if ("hottest".equals(sortBy)) {
            w.orderByDesc(Question::getViewCount);
        } else {
            w.orderByDesc(Question::getCreatedAt);
        }
        int p = page != null ? page : 1;
        int s = size != null ? size : 10;
        return page(new Page<>(p, s), w);
    }

    public Page<Question> myQuestions(Long userId, Integer page, Integer size) {
        int p = page != null ? page : 1;
        int s = size != null ? size : 10;
        LambdaQueryWrapper<Question> w = new LambdaQueryWrapper<>();
        w.eq(Question::getUserId, userId).orderByDesc(Question::getCreatedAt);
        return page(new Page<>(p, s), w);
    }
}
