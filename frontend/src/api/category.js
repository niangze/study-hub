import request from './request'

// 获取所有分类列表
export const getCategoryList = () => request.get('/category/list')

// 获取分类详情
export const getCategoryDetail = (id) => request.get(`/category/${id}`)
