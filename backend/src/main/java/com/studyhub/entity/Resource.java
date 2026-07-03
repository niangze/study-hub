package com.studyhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("resource")
public class Resource {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String category;
    private Long userId;
    private Integer downloadCount;
    private Integer pointsCost;
    private LocalDateTime createdAt;
}
