package com.studyhub.controller;

import com.studyhub.config.Result;
import com.studyhub.entity.Category;
import com.studyhub.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@Tag(name = "分类接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "获取所有分类")
    public Result<List<Category>> list() {
        return Result.success(categoryService.list());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "新增分类")
    public Result<Category> create(@RequestBody Category category) {
        categoryService.save(category);
        return Result.success(category);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "编辑分类")
    public Result<Category> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.updateById(category);
        return Result.success(category);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除分类")
    public Result<?> delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.success();
    }
}
