package com.studyhub.controller;

import com.studyhub.config.Result;
import com.studyhub.entity.Question;
import com.studyhub.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
@Tag(name = "答疑接口")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    @GetMapping("/list")
    @Operation(summary = "问题列表")
    public Result<List<Question>> list() {
        return Result.success(questionService.list());
    }
    
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "按分类查询")
    public Result<List<Question>> listByCategory(@PathVariable Long categoryId) {
        return Result.success(questionService.listByCategory(categoryId));
    }
    
    @GetMapping("/search")
    @Operation(summary = "搜索问题")
    public Result<List<Question>> search(@RequestParam String keyword) {
        return Result.success(questionService.search(keyword));
    }
    
    @PostMapping("/create")
    @Operation(summary = "发布问题")
    public Result<Void> create(@RequestBody Question question) {
        questionService.save(question);
        return Result.success();
    }
    
    @PostMapping("/accept/{answerId}")
    @Operation(summary = "采纳答案")
    public Result<Void> acceptAnswer(@PathVariable Long answerId) {
        questionService.acceptAnswer(answerId);
        return Result.success();
    }
}
