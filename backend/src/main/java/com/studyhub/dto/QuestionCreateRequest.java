package com.studyhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class QuestionCreateRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    
    @NotBlank(message = "内容不能为空")
    private String content;
    
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    
    @Min(value = 0, message = "悬赏积分不能为负数")
    private Integer pointsReward;
}
