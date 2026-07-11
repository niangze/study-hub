import request from './request'

// 获取问题的回答列表
export const getAnswersByQuestion = (questionId) => request.get(`/answer/question/${questionId}`)

// 提交回答
export const createAnswer = (data) => request.post('/answer/create', data)

// 采纳回答（仅问题作者可调用）
export const acceptAnswer = (id) => request.post(`/answer/${id}/accept`)

// 删除回答
export const deleteAnswer = (id) => request.delete(`/answer/${id}`)

// 获取回答总数
export const getAnswerCount = () => request.get('/answer/count')
