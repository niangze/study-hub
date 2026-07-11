import request from './request'

// 获取后台仪表盘统计数据（用户总数、问题总数、回答总数、资源总数）
export const getDashboardStats = () => request.get('/admin/stats')
