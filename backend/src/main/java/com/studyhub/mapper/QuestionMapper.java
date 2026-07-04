package com.studyhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyhub.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    @Update("UPDATE question SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(Long id);

    @Update("UPDATE question SET answer_count = answer_count + 1 WHERE id = #{id}")
    int incrementAnswerCount(Long id);

    @Update("UPDATE question SET answer_count = answer_count - 1 WHERE id = #{id} AND answer_count > 0")
    int decrementAnswerCount(Long id);
}
