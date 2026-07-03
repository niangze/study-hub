import request from './request'

export const getResourceList = () => request.get('/resource/list')
export const getResourcesByCategory = (category) => request.get(`/resource/category/${category}`)
export const uploadResource = (data) => request.post('/resource/upload', data)
