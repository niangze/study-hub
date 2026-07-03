package com.studyhub.controller;

import com.studyhub.config.Result;
import com.studyhub.entity.Resource;
import com.studyhub.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resource")
@Tag(name = "资源接口")
public class ResourceController {
    
    @Autowired
    private ResourceService resourceService;
    
    @GetMapping("/list")
    @Operation(summary = "资源列表")
    public Result<List<Resource>> list() {
        return Result.success(resourceService.list());
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "按分类查询")
    public Result<List<Resource>> listByCategory(@PathVariable String category) {
        return Result.success(resourceService.listByCategory(category));
    }
    
    @PostMapping("/upload")
    @Operation(summary = "上传资源")
    public Result<Void> upload(@RequestBody Resource resource) {
        resourceService.save(resource);
        return Result.success();
    }
}
