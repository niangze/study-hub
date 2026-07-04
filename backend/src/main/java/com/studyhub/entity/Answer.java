package com.studyhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("answer")
public class Answer {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String content;
    private Long questionId;
    private Long userId;
    private Boolean isAccepted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
