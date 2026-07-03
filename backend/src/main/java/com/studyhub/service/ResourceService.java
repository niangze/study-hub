package com.studyhub.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyhub.entity.Resource;
import com.studyhub.mapper.ResourceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService extends ServiceImpl<ResourceMapper, Resource> {
    
    public List<Resource> listByCategory(String category) {
        return lambdaQuery().eq(Resource::getCategory, category).list();
    }
}
