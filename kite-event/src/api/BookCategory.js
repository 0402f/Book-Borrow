import request from '@/utils/request'

// 1. 获取所有图书分类(分页查询)
export const getBookCategoryListService = (params) => {
  return request.get('/bookCategory/page', { params })
}
// 2. 新增图书分类
export const addBookCategoryService = (data) => {
  return request.post('/bookCategory/save', data)
}
// 3.获取分类数量
export const getBookCategoryCountService = () => {
  return request.get('/bookCategory/count')
}
// 4.获取分类列表
export const getBookCategoryList = () => {
  return request.get('/bookCategory/list')
}
// 5.删除图书分类
export const deleteBookCategoryService = (id) => {
  return request.delete(`/bookCategory`, { params: { id } })
}
// 6.更据id更新图书分类
export const updateBookCategoryService = (data) => {
  return request.put('/bookCategory', data)
}
