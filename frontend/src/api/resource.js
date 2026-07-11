import request from './request'

// 分页获取资源列表，支持 keyword(搜索)
export const getResourceList = (params) => request.get('/resource/list', { params })

// 按分类获取资源
export const getResourcesByCategory = (category) => request.get(`/resource/category/${category}`)

// 上传资源
export const uploadResource = (data) => request.post('/resource/create', data)

// 获取资源详情
export const getResourceDetail = (id) => request.get(`/resource/${id}`)

// 下载资源（增加下载计数）
export const downloadResource = (id) => request.post(`/resource/${id}/download`)

// 删除资源
export const deleteResource = (id) => request.delete(`/resource/${id}`)

// 获取资源总数
export const getResourceCount = () => request.get('/resource/count')
