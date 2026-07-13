import request from './request'

// 发表评论
export const createComment = (data) => request.post('/comment/create', data)

// 获取某条回答下的评论列表
export const getCommentsByAnswer = (answerId) => request.get(`/comment/answer/${answerId}`)

// 删除评论
export const deleteComment = (id) => request.delete(`/comment/${id}`)
