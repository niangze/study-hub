import request from './request'

// 获取当前登录用户信息
export const getProfile = () => request.get('/user/profile')

// 更新用户信息（email、avatar）
export const updateProfile = (params) => request.put('/user/profile', null, { params })

// 修改密码
export const updatePassword = (params) => request.post('/user/password', null, { params })

// 通过用户名查看用户信息
export const getUserByUsername = (username) => request.get(`/user/${username}`)
