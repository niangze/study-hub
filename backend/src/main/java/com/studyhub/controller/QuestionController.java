package com.studyhub.controller;

import com.studyhub.config.JwtUtil;
import com.studyhub.config.Result;
import com.studyhub.dto.PageRequest;
import com.studyhub.dto.QuestionCreateRequest;
import com.studyhub.service.QuestionService;
import com.studyhub.vo.PageResult;
import com.studyhub.vo.QuestionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
@Tag(name = "问题管理", description = "问题的增删改查、搜索、排序")
public class QuestionController {

    private final QuestionService questionService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    @Operation(summary = "发布问题", description = "创建新问题并扣除悬赏积分")
    public Result<QuestionVO> create(@Valid @RequestBody QuestionCreateRequest request,
                                      HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        QuestionVO vo = questionService.createQuestion(userId, request);
        return Result.success(vo);
    }

    @GetMapping("/list")
    @Operation(summary = "问题列表", description = "分页查询问题列表，支持搜索和筛选")
    public Result<PageResult<QuestionVO>> list(PageRequest request) {
        return Result.success(questionService.listQuestions(request));
    }

    @GetMapping("/hot")
    @Operation(summary = "热门问题", description = "获取浏览量和回答数最高的问题")
    public Result<List<QuestionVO>> hot(@RequestParam(defaultValue = "5") int limit) {
        return Result.success(questionService.listHotQuestions(limit));
    }

    @GetMapping("/latest")
    @Operation(summary = "最新问题", description = "获取最新发布的问题")
    public Result<List<QuestionVO>> latest(@RequestParam(defaultValue = "5") int limit) {
        return Result.success(questionService.listLatestQuestions(limit));
    }

    @GetMapping("/{id}")
    @Operation(summary = "问题详情", description = "获取问题详细信息（含回答列表）")
    public Result<QuestionVO> detail(@PathVariable Long id) {
        return Result.success(questionService.getQuestionDetail(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除问题", description = "删除问题（仅作者或管理员）")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        questionService.deleteQuestion(id, userId);
        return Result.success();
    }

    @PostMapping("/{id}/close")
    @Operation(summary = "关闭问题", description = "手动关闭问题（仅作者）")
    public Result<Void> close(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        questionService.closeQuestion(id, userId);
        return Result.success();
    }

    @GetMapping("/count")
    @Operation(summary = "问题统计", description = "获取问题总数或按状态统计")
    public Result<Long> count(@RequestParam(required = false) String status) {
        return Result.success(questionService.countByStatus(status));
    }
}
