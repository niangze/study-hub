import request from './request'

// 分页获取问题列表，支持 keyword(搜索)、categoryId(分类筛选)、status(状态筛选)
export const getQuestionList = (params) => request.get('/question/list', { params })

// 获取热门问题
export const getHotQuestions = (limit = 5) => request.get('/question/hot', { params: { limit } })

// 获取最新问题
export const getLatestQuestions = (limit = 5) => request.get('/question/latest', { params: { limit } })

// 获取问题详情
export const getQuestionDetail = (id) => request.get(`/question/${id}`)

// 创建问题
export const createQuestion = (data) => request.post('/question/create', data)

// 删除问题
export const deleteQuestion = (id) => request.delete(`/question/${id}`)

// 关闭问题
export const closeQuestion = (id) => request.post(`/question/${id}/close`)

// 获取问题总数
export const getQuestionCount = (status) => request.get('/question/count', { params: { status } })
