package com.studyhub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.studyhub.dto.CommentRequest;
import com.studyhub.entity.Comment;
import com.studyhub.mapper.CommentMapper;
import com.studyhub.vo.CommentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    public CommentVO createComment(Long userId, CommentRequest request) {
        Comment comment = new Comment();
        comment.setAnswerId(request.getAnswerId());
        comment.setUserId(userId);
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setDeleted(0);
        commentMapper.insert(comment);
        return toVO(comment);
    }

    public List<CommentVO> getCommentsByAnswerId(Long answerId) {
        return commentMapper.selectByAnswerId(answerId).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    public void deleteComment(Long commentId, Long userId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("id", commentId).eq("user_id", userId);
        Comment comment = new Comment();
        comment.setDeleted(1);
        commentMapper.update(comment, wrapper);
    }

    private CommentVO toVO(Comment comment) {
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(comment, vo);
        return vo;
    }
}
