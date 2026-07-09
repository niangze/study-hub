package com.studyhub.vo;

import lombok.Data;

@Data
public class DashboardStatsVO {
    private Long userCount;
    private Long questionCount;
    private Long answerCount;
    private Long resourceCount;
    private Long todayQuestionCount;
    private Long todayAnswerCount;
    private Long pendingQuestionCount;
    private Long resolvedQuestionCount;
}
