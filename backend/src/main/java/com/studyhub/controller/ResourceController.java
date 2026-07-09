package com.studyhub.controller;

import com.studyhub.config.JwtUtil;
import com.studyhub.config.Result;
import com.studyhub.dto.PageRequest;
import com.studyhub.dto.ResourceCreateRequest;
import com.studyhub.service.ResourceService;
import com.studyhub.vo.PageResult;
import com.studyhub.vo.ResourceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resource")
@RequiredArgsConstructor
@Tag(name = "资源管理", description = "学习资源的增删改查、下载")
public class ResourceController {

    private final ResourceService resourceService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    @Operation(summary = "上传资源", description = "创建新的学习资源")
    public Result<ResourceVO> create(@Valid @RequestBody ResourceCreateRequest request,
                                      HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        ResourceVO vo = resourceService.createResource(userId, request);
        return Result.success(vo);
    }

    @GetMapping("/list")
    @Operation(summary = "资源列表", description = "分页查询资源列表，支持搜索")
    public Result<PageResult<ResourceVO>> list(PageRequest request) {
        return Result.success(resourceService.listResources(request));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "分类资源", description = "按分类获取资源列表")
    public Result<List<ResourceVO>> byCategory(@PathVariable String category) {
        return Result.success(resourceService.listByCategory(category));
    }

    @GetMapping("/{id}")
    @Operation(summary = "资源详情", description = "获取资源详细信息")
    public Result<ResourceVO> detail(@PathVariable Long id) {
        return Result.success(resourceService.getResourceDetail(id));
    }

    @PostMapping("/{id}/download")
    @Operation(summary = "下载资源", description = "增加资源下载计数")
    public Result<Void> download(@PathVariable Long id) {
        resourceService.incrementDownloadCount(id);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除资源", description = "删除资源（仅作者或管理员）")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        resourceService.deleteResource(id, userId);
        return Result.success();
    }

    @GetMapping("/count")
    @Operation(summary = "资源统计", description = "获取资源总数")
    public Result<Long> count() {
        return Result.success(resourceService.countResources());
    }
}
