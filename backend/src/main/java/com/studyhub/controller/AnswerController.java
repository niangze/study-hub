package com.studyhub.controller;

import com.studyhub.config.Result;
import com.studyhub.entity.Answer;
import com.studyhub.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping
    @Operation(summary = "提交回答")
    public Result<Answer> submit(@RequestBody Answer answer) {
        return Result.success(answerService.submit(
                getCurrentUserId(), answer.getQuestionId(), answer.getContent()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑回答")
    public Result<Answer> update(@PathVariable Long id, @RequestBody Answer answer) {
        return Result.success(answerService.updateAnswer(id, getCurrentUserId(), answer.getContent()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除回答")
    public Result<?> delete(@PathVariable Long id) {
        answerService.deleteAnswer(id, getCurrentUserId());
        return Result.success();
    }

    @PutMapping("/accept/{id}")
    @Operation(summary = "采纳最佳答案")
    public Result<?> accept(@PathVariable Long id) {
        answerService.acceptAnswer(id, getCurrentUserId());
        return Result.success();
    }

    @GetMapping("/my")
    @Operation(summary = "我的回答")
    public Result<List<Answer>> myAnswers() {
        return Result.success(answerService.myAnswers(getCurrentUserId()));
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
