package com.studyhub.controller;

import com.studyhub.config.Result;
import com.studyhub.entity.User;
import com.studyhub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息")
    public Result<User> currentUser() {
        Long userId = getCurrentUserId();
        return Result.success(userService.getCurrentUser(userId));
    }

    @PutMapping("/info")
    @Operation(summary = "更新个人信息")
    public Result<?> updateInfo(@RequestParam(required = false) String email,
                                @RequestParam(required = false) String studentId) {
        userService.updateInfo(getCurrentUserId(), email, studentId);
        return Result.success();
    }

    @PutMapping("/avatar")
    @Operation(summary = "更新头像")
    public Result<?> updateAvatar(@RequestParam String avatarUrl) {
        userService.updateAvatar(getCurrentUserId(), avatarUrl);
        return Result.success();
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Result<?> changePassword(@RequestParam String oldPassword,
                                    @RequestParam String newPassword) {
        userService.changePassword(getCurrentUserId(), oldPassword, newPassword);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
