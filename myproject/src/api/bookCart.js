import http from "../utils/http";
// 获取分裂信息
export const getSplitInfo = () => {
  return http.get("/bookCategory/list");
};
// 更据分类id获取分类下的书籍
export const getBookByCategoryIdService = (categoryId, status) => {
  return http.get("/book/list", { categoryId, status });
};
// 更据名字模糊搜索
export const getBookByNameService = (name) => {
  return http.get("/book/GetBookByLikeName", { name });
};
// 更据id获取书籍详情
export const getBookDetailByIdService = (id) => {
  return http.get("/book/GetBookById", { id });
};
