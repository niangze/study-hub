package com.studyhub.controller;

import com.studyhub.config.JwtUtil;
import com.studyhub.config.Result;
import com.studyhub.dto.LikeRequest;
import com.studyhub.service.LikeService;
import com.studyhub.vo.LikeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
@Tag(name = "点赞管理", description = "问题和答案的点赞功能")
public class LikeController {

    private final LikeService likeService;
    private final JwtUtil jwtUtil;

    @PostMapping("/toggle")
    @Operation(summary = "点赞/取消点赞")
    public Result<LikeVO> toggleLike(@Valid @RequestBody LikeRequest request,
                                      HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        return Result.success(likeService.toggleLike(userId, request));
    }

    @GetMapping("/info")
    @Operation(summary = "点赞信息", description = "获取目标的点赞数和当前用户是否点赞")
    public Result<LikeVO> getLikeInfo(@RequestParam Long targetId,
                                       @RequestParam String targetType,
                                       HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        return Result.success(likeService.getLikeInfo(userId, targetId, targetType.toUpperCase()));
    }
}
