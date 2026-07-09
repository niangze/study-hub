package com.studyhub.controller;

import com.studyhub.config.Result;
import com.studyhub.service.DashboardService;
import com.studyhub.vo.DashboardStatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "后台管理", description = "数据统计、系统管理")
public class AdminController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "统计数据", description = "获取仪表盘统计数据")
    public Result<DashboardStatsVO> stats() {
        return Result.success(dashboardService.getStats());
    }
}
