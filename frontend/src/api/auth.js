import request from './request'

export const register = (data) => request.post('/auth/register', null, { params: data })
export const login = (data) => request.post('/auth/login', null, { params: data })
