import request from '@/utils/request'
// 获取首页信息
export const getIndexInfo = () => {
  return request.get('/index')
}
