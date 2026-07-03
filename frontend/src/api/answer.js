import request from './request'

export const getAnswersByQuestion = (questionId) => request.get(`/answer/question/${questionId}`)
export const createAnswer = (data) => request.post('/answer/create', data)
