package com.studyhub.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionVO {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private String categoryName;
    private Long userId;
    private String username;
    private String userAvatar;
    private Integer pointsReward;
    private String status;
    private Integer answerCount;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AnswerVO> answers;
}
