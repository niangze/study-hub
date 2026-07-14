package com.studyhub.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String email;
    private String avatar;
    private Integer points;
    private String role;
    private String studentId;
    private LocalDateTime createdAt;
    private Integer questionCount;
    private Integer answerCount;
}
