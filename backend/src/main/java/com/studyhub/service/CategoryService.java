package com.studyhub.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyhub.entity.Category;
import com.studyhub.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    public List<Category> listAll() {
        return list();
    }

    public Category getById(Long id) {
        return super.getById(id);
    }
}
