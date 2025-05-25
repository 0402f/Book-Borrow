package com.slt.slt.service;

import com.slt.slt.Result.ResultPage;
import com.slt.slt.domain.DTO.BookCategoryDTO;
import com.slt.slt.domain.DTO.CategoryDTO;
import com.slt.slt.domain.POJO.BookCategory;

import java.util.List;

public interface BookCategoryService {

    /**
     *  分页查询书籍分类列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResultPage<BookCategoryDTO> getBookCategoryPage(Integer pageNum, Integer pageSize);

    /**
     *  新增书籍分类
     * @param bookCategory
     */
    void save(BookCategory bookCategory);

    /**
     *  查询书籍分类数量
     * @return
     */
    Long getBookCategoryCount();

    /**
     *  查询分类列表
     * @return
     */
    List<CategoryDTO> getCategoryList();

    /**
     *  根据id删除书籍分类
     * @param id
     */
    void deleteBookCategoryById(Integer id);

    /**
     * 更据id来更新书籍分类信息
     * @param bookCategory
     */
    void updateBookCategoryById(BookCategory bookCategory);
}
