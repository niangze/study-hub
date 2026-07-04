package com.studyhub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studyhub.config.Result;
import com.studyhub.entity.Question;
import com.studyhub.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/question")
@Tag(name = "答疑接口")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/list")
    @Operation(summary = "分页问题列表")
    public Result<Page<Question>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "newest") String sortBy,
            @RequestParam(required = false) String status) {
        return Result.success(questionService.list(page, size, keyword, categoryId, sortBy, status));
    }

    @GetMapping("/my")
    @Operation(summary = "我的问题")
    public Result<Page<Question>> my(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(questionService.myQuestions(getCurrentUserId(), page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "问题详情")
    public Result<Question> detail(@PathVariable Long id) {
        return Result.success(questionService.getDetail(id));
    }

    @PostMapping
    @Operation(summary = "发布问题")
    public Result<Question> create(@RequestBody Question question) {
        return Result.success(questionService.create(getCurrentUserId(),
                question.getTitle(), question.getContent(),
                question.getCategoryId(), question.getPointsReward()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑问题")
    public Result<Question> update(@PathVariable Long id, @RequestBody Question question) {
        return Result.success(questionService.updateQuestion(id, getCurrentUserId(),
                question.getTitle(), question.getContent(), question.getCategoryId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除问题")
    public Result<?> delete(@PathVariable Long id) {
        questionService.deleteQuestion(id, getCurrentUserId());
        return Result.success();
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
