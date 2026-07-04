package com.studyhub.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyhub.entity.Category;
import com.studyhub.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {
    // BaseMapper 已提供全部CRUD，无需额外代码
}
