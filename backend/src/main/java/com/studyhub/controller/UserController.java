package com.studyhub.controller;

import com.studyhub.config.Result;
import com.studyhub.entity.User;
import com.studyhub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "用户接口")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }
}
