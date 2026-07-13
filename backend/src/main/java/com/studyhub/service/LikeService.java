package com.studyhub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.studyhub.dto.LikeRequest;
import com.studyhub.entity.Like;
import com.studyhub.mapper.LikeMapper;
import com.studyhub.vo.LikeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeMapper likeMapper;

    @Transactional
    public LikeVO toggleLike(Long userId, LikeRequest request) {
        String targetType = request.getTargetType().toUpperCase();
        QueryWrapper<Like> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("target_id", request.getTargetId())
               .eq("target_type", targetType);
        
        Like existing = likeMapper.selectOne(wrapper);
        if (existing != null) {
            likeMapper.delete(wrapper);
        } else {
            Like like = new Like();
            like.setUserId(userId);
            like.setTargetId(request.getTargetId());
            like.setTargetType(targetType);
            likeMapper.insert(like);
        }
        
        return getLikeInfo(userId, request.getTargetId(), targetType);
    }

    public LikeVO getLikeInfo(Long userId, Long targetId, String targetType) {
        LikeVO vo = new LikeVO();
        vo.setTargetId(targetId);
        vo.setTargetType(targetType);
        vo.setLikeCount(likeMapper.countByTarget(targetId, targetType));
        vo.setIsLiked(userId != null && likeMapper.exists(userId, targetId, targetType) > 0);
        return vo;
    }
}
