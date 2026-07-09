package com.studyhub.vo;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private List<T> list;
    private Long total;
    private Long page;
    private Long size;
    private Long pages;
    
    public static <T> PageResult<T> of(List<T> list, Long total, Long page, Long size) {
        PageResult<T> result = new PageResult<>();
        result.setList(list);
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        result.setPages((total + size - 1) / size);
        return result;
    }
}
