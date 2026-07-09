package com.studyhub.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResourceVO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Long userId;
    private String username;
    private Integer downloadCount;
    private Integer pointsCost;
    private String fileUrl;
    private Long fileSize;
    private LocalDateTime createdAt;
}
