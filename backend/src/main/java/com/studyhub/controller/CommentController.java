package com.studyhub.controller;

import com.studyhub.config.JwtUtil;
import com.studyhub.config.Result;
import com.studyhub.dto.CommentRequest;
import com.studyhub.service.CommentService;
import com.studyhub.vo.CommentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@Tag(name = "评论管理", description = "答案的评论回复功能")
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    @Operation(summary = "发表评论")
    public Result<CommentVO> create(@Valid @RequestBody CommentRequest request,
                                     HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        return Result.success(commentService.createComment(userId, request));
    }

    @GetMapping("/answer/{answerId}")
    @Operation(summary = "评论列表", description = "获取某条回答下的所有评论")
    public Result<List<CommentVO>> listByAnswer(@PathVariable Long answerId) {
        return Result.success(commentService.getCommentsByAnswerId(answerId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评论")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        commentService.deleteComment(id, userId);
        return Result.success();
    }
}
