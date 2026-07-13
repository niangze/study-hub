import request from './request'

// 关注/取消关注
export const toggleFollow = (userId) => request.post(`/follow/${userId}`)

// 查询关注状态
export const isFollowing = (userId) => request.get(`/follow/status/${userId}`)

// 获取关注统计（粉丝数 + 关注数）
export const getFollowCounts = (userId) => request.get(`/follow/counts/${userId}`)

// 获取我的关注列表
export const getFollowingList = () => request.get('/follow/following/list')
