package com.studyhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyhub.entity.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper extends BaseMapper<Like> {

    @Select("SELECT COUNT(*) FROM likes WHERE target_id = #{targetId} AND target_type = #{targetType}")
    int countByTarget(@Param("targetId") Long targetId, @Param("targetType") String targetType);

    @Select("SELECT COUNT(*) FROM likes WHERE target_id = #{targetId} AND target_type = #{targetType} AND user_id = #{userId}")
    int exists(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("targetType") String targetType);
}
