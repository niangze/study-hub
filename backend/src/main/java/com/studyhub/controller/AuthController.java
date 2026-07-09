package com.studyhub.controller;

import com.studyhub.config.Result;
import com.studyhub.dto.LoginRequest;
import com.studyhub.dto.RegisterRequest;
import com.studyhub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户注册、登录、JWT认证")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户并返回JWT令牌")
    public Result<String> register(@Valid @RequestBody RegisterRequest request) {
        String token = userService.register(request);
        return Result.success(token);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名密码登录并返回JWT令牌")
    public Result<String> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        return Result.success(token);
    }
}
