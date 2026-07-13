package com.studyhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyhub.entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

    @Select("SELECT COUNT(*) FROM follow WHERE following_id = #{userId}")
    int countFollowers(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM follow WHERE follower_id = #{userId}")
    int countFollowing(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM follow WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    int isFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Select("SELECT u.* FROM user u JOIN follow f ON u.id = f.following_id WHERE f.follower_id = #{userId}")
    List<com.studyhub.entity.User> selectFollowingList(@Param("userId") Long userId);
}
