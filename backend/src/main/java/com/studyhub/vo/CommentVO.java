package com.studyhub.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentVO {
    private Long id;
    private Long answerId;
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
}
