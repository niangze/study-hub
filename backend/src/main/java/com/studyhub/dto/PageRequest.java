package com.studyhub.dto;

import lombok.Data;

@Data
public class PageRequest {
    private Long page = 1L;
    private Long size = 10L;
    private String keyword;
    private Long categoryId;
    private String sort = "created_at";
    private String order = "desc";
    
    public Long getOffset() {
        return (page - 1) * size;
    }
}
