import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api/v1',
  timeout: 10000
})

request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = 'Bearer ' + token
  }
  return config
})

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message))
    }
    return res.data
  },
  error => {
    const status = error.response?.status
    const message = error.response?.data?.message || error.message || '网络错误'

    // 401 未登录或 Token 过期
    if (status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      localStorage.removeItem('token')
      // 延迟跳转，避免在当前请求处理过程中改变路由
      setTimeout(() => {
        if (window.location.pathname !== '/login') {
          window.location.href = '/login'
        }
      }, 500)
      return Promise.reject(new Error('登录已过期'))
    }

    // 403 权限不足
    if (status === 403) {
      ElMessage.error('权限不足')
      return Promise.reject(new Error('权限不足'))
    }

    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default request
