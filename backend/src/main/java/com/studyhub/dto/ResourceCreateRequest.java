package com.studyhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ResourceCreateRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    
    private String description;
    
    @NotBlank(message = "分类不能为空")
    private String category;
    
    @Min(value = 0, message = "积分消耗不能为负数")
    private Integer pointsCost;
    
    private String fileUrl;
    private Long fileSize;
}
