package com.studyhub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.studyhub.entity.Follow;
import com.studyhub.entity.User;
import com.studyhub.mapper.FollowMapper;
import com.studyhub.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowMapper followMapper;

    @Transactional
    public boolean toggleFollow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new RuntimeException("不能关注自己");
        }
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        wrapper.eq("follower_id", followerId).eq("following_id", followingId);
        Follow existing = followMapper.selectOne(wrapper);
        if (existing != null) {
            followMapper.delete(wrapper);
            return false;
        } else {
            Follow follow = new Follow();
            follow.setFollowerId(followerId);
            follow.setFollowingId(followingId);
            follow.setCreatedAt(LocalDateTime.now());
            followMapper.insert(follow);
            return true;
        }
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followMapper.isFollowing(followerId, followingId) > 0;
    }

    public int getFollowerCount(Long userId) {
        return followMapper.countFollowers(userId);
    }

    public int getFollowingCount(Long userId) {
        return followMapper.countFollowing(userId);
    }

    public List<UserVO> getFollowingList(Long userId) {
        List<User> users = followMapper.selectFollowingList(userId);
        return users.stream().map(u -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(u, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
