package com.studyhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyhub.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    @Select("SELECT COUNT(*) FROM question WHERE status = 'OPEN'")
    Long selectPendingCount();

    @Select("SELECT COUNT(*) FROM question WHERE status = 'CLOSED'")
    Long selectResolvedCount();
}
