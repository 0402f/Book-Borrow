import request from '@/utils/request'
// 登录接口
export const loginService = (data) => {
  return request.post('/user/login', data)
}
// 注册接口
export const registerService = (data) => {
  return request.post('/user/register', data)
}
// 获取所有用户信息
export const getUserInfoService = (params) => {
  return request.get('/user/list', { params })
}

// 获取当前用户信息
export const getCurrentUserInfoService = () => {
  return request.get('/user/userInfo')
}
// 删除用户
export const deleteUserService = (id) => {
  return request.delete('/user', { params: { id } })
}
// 更据id获取用户信息
export const getUserByIdService = (UserId) => {
  return request.get('/user', { params: { UserId } })
}
// 获取用户数量
export const getUserCountService = () => {
  return request.get('/user/Count')
}
