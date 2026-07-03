import request from './request'

export const getQuestionList = () => request.get('/question/list')
export const getQuestionsByCategory = (categoryId) => request.get(`/question/category/${categoryId}`)
export const searchQuestions = (keyword) => request.get('/question/search', { params: { keyword } })
export const createQuestion = (data) => request.post('/question/create', data)
export const acceptAnswer = (answerId) => request.post(`/question/accept/${answerId}`)
