package com.studyhub.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnswerVO {
    private Long id;
    private String content;
    private Long questionId;
    private Long userId;
    private String username;
    private String userAvatar;
    private Boolean isAccepted;
    private LocalDateTime createdAt;
}
