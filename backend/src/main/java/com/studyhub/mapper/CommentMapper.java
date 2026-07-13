package com.studyhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyhub.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT * FROM comment WHERE answer_id = #{answerId} AND deleted = 0 ORDER BY created_at DESC")
    List<Comment> selectByAnswerId(@Param("answerId") Long answerId);
}
