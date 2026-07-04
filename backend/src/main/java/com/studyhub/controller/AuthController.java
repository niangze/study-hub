package com.studyhub.controller;

import com.studyhub.config.Result;
import com.studyhub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "认证接口")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<String> register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password) || !StringUtils.hasText(email)) {
            return Result.error(400, "用户名、密码和邮箱不能为空");
        }
        if (password.length() < 6) {
            return Result.error(400, "密码长度不能少于6位");
        }
        String token = userService.register(username, password, email);
        return Result.success(token);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<String> login(
            @RequestParam String username,
            @RequestParam String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return Result.error(400, "用户名和密码不能为空");
        }
        String token = userService.login(username, password);
        return Result.success(token);
    }
}
