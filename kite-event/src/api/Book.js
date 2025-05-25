import request from '@/utils/request'

// 1.. 获取所有书籍库存数
export const getBookStockervice = () => {
  return request.get('/book/Count')
}

// 2. 根据分类id来获取该分类下的书籍
export const getBookByCategoryIdService = (categoryId, status) => {
  return request.get('/book/list', { params: { categoryId, status } })
}

// 3.新增书籍
export const addBookService = (data) => {
  return request.post('/book/save', data)
}
// 4.获取图片上传地址
export const uploadFileService = (formData) => {
  return request.post(`/upload`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
// 5.更新书籍信息
export const updateBookService = (data) => {
  return request.post('/book/update', data)
}
// 6.上架,下架书籍
export const UpOrDownBookService = (id, status) => {
  return request.post('/book/UporDown', {}, { params: { id, status } })
}
