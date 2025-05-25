package com.slt.slt.service;

import com.slt.slt.domain.POJO.Book;
import com.slt.slt.domain.VO.BookVO;

import java.util.List;

public interface BookService {

    /**
     * 获取书籍数量
     * @return
     */
    Long GetBookCount();

    /**
     * 根据分类id获取书籍列表
     * @param categoryId
     * @return
     */
    List<Book> GetBookListByCategoryId(Integer categoryId,Integer status);

    /**
     * 新增书籍
     * @param bookVO
     */
    void SaveBook(BookVO bookVO);

    /**
     * 更新书籍信息
     * @param bookVO
     */
    void UpdateBook(BookVO bookVO);

    /**
     * 下架图书
     * @param id
     */
    void UpOrDownBook(Integer id, Integer status);

    /**
     * 更据书籍名或者分类名模糊查询
     * @param name
     * @return
     */
    List<Book> GetBookByLikeName(String name);

    /**
     *  更据id获取书籍信息
     * @param id
     * @return
     */
    Book GetBookById(Integer id);
}
