package com.studyhub.controller;

import com.studyhub.config.JwtUtil;
import com.studyhub.config.Result;
import com.studyhub.dto.AnswerCreateRequest;
import com.studyhub.service.AnswerService;
import com.studyhub.vo.AnswerVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/answer")
@RequiredArgsConstructor
@Tag(name = "回答管理", description = "回答的创建、采纳、查询")
public class AnswerController {

    private final AnswerService answerService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    @Operation(summary = "提交回答", description = "为指定问题提交回答")
    public Result<AnswerVO> create(@Valid @RequestBody AnswerCreateRequest request,
                                    HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        AnswerVO vo = answerService.createAnswer(userId, request);
        return Result.success(vo);
    }

    @GetMapping("/question/{questionId}")
    @Operation(summary = "回答列表", description = "获取指定问题的所有回答")
    public Result<List<AnswerVO>> listByQuestion(@PathVariable Long questionId) {
        return Result.success(answerService.listAnswersByQuestion(questionId));
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "采纳回答", description = "采纳指定回答为最佳答案（仅问题作者）")
    public Result<Void> accept(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        answerService.acceptAnswer(id, userId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除回答", description = "删除回答（仅作者或管理员）")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        answerService.deleteAnswer(id, userId);
        return Result.success();
    }

    @GetMapping("/count")
    @Operation(summary = "回答统计", description = "获取回答总数")
    public Result<Long> count() {
        return Result.success(answerService.countAnswers());
    }
}
