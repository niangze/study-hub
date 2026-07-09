package com.studyhub.controller;

import com.studyhub.config.JwtUtil;
import com.studyhub.config.Result;
import com.studyhub.service.UserService;
import com.studyhub.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户信息、积分、个人中心")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/profile")
    @Operation(summary = "个人信息", description = "获取当前登录用户信息")
    public Result<UserVO> profile(HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromRequest(request);
        return Result.success(userService.getProfile(userId));
    }

    @PutMapping("/profile")
    @Operation(summary = "更新信息", description = "更新用户邮箱和头像")
    public Result<UserVO> updateProfile(@RequestParam(required = false) String email,
                                         @RequestParam(required = false) String avatar,
                                         HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromRequest(request);
        return Result.success(userService.updateProfile(userId, email, avatar));
    }

    @PostMapping("/password")
    @Operation(summary = "修改密码", description = "修改用户登录密码")
    public Result<Void> updatePassword(@RequestParam String oldPassword,
                                        @RequestParam String newPassword,
                                        HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromRequest(request);
        userService.updatePassword(userId, oldPassword, newPassword);
        return Result.success();
    }

    @GetMapping("/{username}")
    @Operation(summary = "查看用户", description = "通过用户名查看用户信息")
    public Result<UserVO> getByUsername(@PathVariable String username) {
        UserVO vo = userService.getByUsername(username);
        if (vo == null) {
            return Result.error("用户不存在");
        }
        return Result.success(vo);
    }
}
