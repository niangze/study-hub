package com.studyhub.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyhub.dto.PageRequest;
import com.studyhub.dto.ResourceCreateRequest;
import com.studyhub.entity.Resource;
import com.studyhub.entity.User;
import com.studyhub.mapper.ResourceMapper;
import com.studyhub.mapper.UserMapper;
import com.studyhub.vo.PageResult;
import com.studyhub.vo.ResourceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceService extends ServiceImpl<ResourceMapper, Resource> {

    private final UserMapper userMapper;

    @Transactional
    public ResourceVO createResource(Long userId, ResourceCreateRequest request) {
        Resource resource = new Resource();
        BeanUtils.copyProperties(request, resource);
        resource.setUserId(userId);
        resource.setDownloadCount(0);
        
        save(resource);
        return convertToVO(resource);
    }

    public PageResult<ResourceVO> listResources(PageRequest request) {
        LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
        
        // Keyword search
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            wrapper.like(Resource::getTitle, request.getKeyword());
        }
        
        // Sort
        if ("asc".equalsIgnoreCase(request.getOrder())) {
            wrapper.orderByAsc(Resource::getCreatedAt);
        } else {
            wrapper.orderByDesc(Resource::getCreatedAt);
        }
        
        Page<Resource> page = new Page<>(request.getPage(), request.getSize());
        page(page, wrapper);
        
        List<ResourceVO> list = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return PageResult.of(list, page.getTotal(), request.getPage(), request.getSize());
    }

    public List<ResourceVO> listByCategory(String category) {
        LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resource::getCategory, category)
               .orderByDesc(Resource::getDownloadCount);
        
        return list(wrapper).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    public ResourceVO getResourceDetail(Long id) {
        Resource resource = getById(id);
        if (resource == null) {
            throw new RuntimeException("资源不存在");
        }
        return convertToVO(resource);
    }

    @Transactional
    public void incrementDownloadCount(Long id) {
        Resource resource = getById(id);
        if (resource != null) {
            resource.setDownloadCount(resource.getDownloadCount() + 1);
            updateById(resource);
        }
    }

    @Transactional
    public void deleteResource(Long id, Long userId) {
        Resource resource = getById(id);
        if (resource == null) {
            throw new RuntimeException("资源不存在");
        }
        
        if (!resource.getUserId().equals(userId)) {
            User user = userMapper.selectById(userId);
            if (!"ADMIN".equals(user.getRole())) {
                throw new RuntimeException("无权删除");
            }
        }
        
        removeById(id);
    }

    public Long countResources() {
        return count();
    }

    private ResourceVO convertToVO(Resource resource) {
        ResourceVO vo = new ResourceVO();
        BeanUtils.copyProperties(resource, vo);
        
        User user = userMapper.selectById(resource.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
        }
        
        return vo;
    }
}
