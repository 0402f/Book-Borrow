import http from "../utils/http";

// 借阅图书
export const borrowBook = (data) => {
  return http.post("/borrow/BorrowBook", data);
};

// 获取借阅记录
export const getBorrowHistory = () => {
  return http.get("/borrow/getBorrowList");
};

// 删除借阅记录
export const deleteBorrowHistory = (borrowId) => {
  return http.post("/borrow/deleteBorrow?borrowId=" + borrowId);
};
// 更据Id查询借阅记录
export const getBorrowHistoryById = (borrowId) => {
  return http.get(`/borrow/getBorrowById?borrowId=${borrowId}`);
}

/**
 * 归还书籍
 * @param {Object} data - 包含借阅ID和归还数量的对象
 * @param {number} data.borrowId - 借阅ID
 * @param {number} data.returnCounts - 归还数量
 * @returns {Promise}
 */
export const returnBorrowedBook = (data) => {
  return http.post(`/borrow/ReturnBook?borrowId=${data.borrowId}&returnCounts=${data.returnCounts}`);
}
