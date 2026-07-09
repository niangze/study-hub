package com.studyhub.service;

import com.studyhub.mapper.AnswerMapper;
import com.studyhub.mapper.QuestionMapper;
import com.studyhub.mapper.ResourceMapper;
import com.studyhub.mapper.UserMapper;
import com.studyhub.vo.DashboardStatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserMapper userMapper;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;
    private final ResourceMapper resourceMapper;

    public DashboardStatsVO getStats() {
        DashboardStatsVO stats = new DashboardStatsVO();
        stats.setUserCount((long) userMapper.selectCount(null));
        stats.setQuestionCount((long) questionMapper.selectCount(null));
        stats.setAnswerCount((long) answerMapper.selectCount(null));
        stats.setResourceCount((long) resourceMapper.selectCount(null));
        stats.setPendingQuestionCount(questionMapper.selectPendingCount());
        stats.setResolvedQuestionCount(questionMapper.selectResolvedCount());
        return stats;
    }
}
