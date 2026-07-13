package com.studyhub.controller;

import com.studyhub.config.JwtUtil;
import com.studyhub.config.Result;
import com.studyhub.service.FollowService;
import com.studyhub.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/follow")
@RequiredArgsConstructor
@Tag(name = "关注管理", description = "用户关注/取关功能")
public class FollowController {

    private final FollowService followService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{userId}")
    @Operation(summary = "关注/取消关注")
    public Result<Boolean> toggleFollow(@PathVariable Long userId,
                                         HttpServletRequest httpRequest) {
        Long currentUserId = jwtUtil.getUserIdFromRequest(httpRequest);
        boolean isFollowing = followService.toggleFollow(currentUserId, userId);
        return Result.success(isFollowing);
    }

    @GetMapping("/status/{userId}")
    @Operation(summary = "关注状态")
    public Result<Boolean> isFollowing(@PathVariable Long userId,
                                        HttpServletRequest httpRequest) {
        Long currentUserId = jwtUtil.getUserIdFromRequest(httpRequest);
        return Result.success(followService.isFollowing(currentUserId, userId));
    }

    @GetMapping("/counts/{userId}")
    @Operation(summary = "关注统计", description = "获取用户的粉丝数和关注数")
    public Result<Map<String, Integer>> getCounts(@PathVariable Long userId) {
        return Result.success(Map.of(
            "followers", followService.getFollowerCount(userId),
            "following", followService.getFollowingCount(userId)
        ));
    }

    @GetMapping("/following/list")
    @Operation(summary = "我的关注列表")
    public Result<List<UserVO>> getFollowingList(HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        return Result.success(followService.getFollowingList(userId));
    }
}
