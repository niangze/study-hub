package com.studyhub.controller;

import com.studyhub.config.Result;
import com.studyhub.entity.Answer;
import com.studyhub.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/answer")
@Tag(name = "回答接口")
public class AnswerController {
    
    @Autowired
    private AnswerService answerService;
    
    @GetMapping("/question/{questionId}")
    @Operation(summary = "获取问题的回答列表")
    public Result<List<Answer>> listByQuestion(@PathVariable Long questionId) {
        return Result.success(answerService.listByQuestion(questionId));
    }
    
    @PostMapping("/create")
    @Operation(summary = "提交回答")
    public Result<Void> create(@RequestBody Answer answer) {
        answerService.save(answer);
        return Result.success();
    }
}
