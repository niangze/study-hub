package com.studyhub.vo;

import lombok.Data;

@Data
public class LikeVO {
    private Long targetId;
    private String targetType;
    private Integer likeCount;
    private Boolean isLiked;
}
