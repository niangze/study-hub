import request from './request'

// 点赞/取消点赞
targetType: 'QUESTION' | 'ANSWER'
export const toggleLike = (data) => request.post('/like/toggle', data)

// 获取点赞信息（点赞数 + 当前用户是否点赞）
export const getLikeInfo = (targetId, targetType) => request.get('/like/info', {
  params: { targetId, targetType }
})
