package com.slt.slt.service.serviceImpl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.slt.slt.Result.ResultPage;
import com.slt.slt.domain.DTO.BookCategoryDTO;
import com.slt.slt.domain.DTO.CategoryDTO;
import com.slt.slt.domain.POJO.BookCategory;
import com.slt.slt.mapper.BookCategoryMapper;
import com.slt.slt.service.BookCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookCategoryServiceImpl implements BookCategoryService {


    private final BookCategoryMapper bookCategoryMapper;

    /**
     * 分页查询书籍分类列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResultPage<BookCategoryDTO> getBookCategoryPage(Integer pageNum, Integer pageSize) {
        // 1.设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        // 2.从数据库中查询所有分类
        List<BookCategoryDTO> bookCategoryList = bookCategoryMapper.getBookCategoryList();

        // 遍历分类列表，查询每个分类下的书籍数量并设置到 DTO 中
        for (BookCategoryDTO categoryDTO : bookCategoryList) {
            int categoryId = categoryDTO.getId();
            Integer bookCount = bookCategoryMapper.getBookCategoryCountById(categoryId);
            categoryDTO.setBookCount(bookCount);
        }
        Page<BookCategoryDTO> page = (Page<BookCategoryDTO>) bookCategoryList;

        // 封装到自定义的 ResultPage 中
        ResultPage<BookCategoryDTO> resultPage = new ResultPage<>();
        resultPage.setTotal(page.getTotal());
        resultPage.setItems(page.getResult());
        return resultPage;
    }

    /**
     *  新增书籍分类
     * @param bookCategory
     */
    @Override
    public void save(BookCategory bookCategory) {
        // 1.判断分类名称是否重复
        BookCategory category = bookCategoryMapper.getBookCategoryByName(bookCategory.getName());
        if (category != null) {
            throw new RuntimeException("分类名称重复");
        }
        bookCategoryMapper.save(bookCategory);
    }

    /**
     *  查询书籍分类数量
     * @return
     */
    @Override
    @Transactional // 开启事务
    public Long getBookCategoryCount() {
        return bookCategoryMapper.getBookCategoryCount();
    }

    /**
     *  查询分类列表
     * @return
     */
    @Override
    public List<CategoryDTO> getCategoryList() {
        List<BookCategory> list = bookCategoryMapper.getCategoryList();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (BookCategory bookCategory : list) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(bookCategory.getId());
            categoryDTO.setName(bookCategory.getName());
            categoryDTOList.add(categoryDTO);
        }
        return categoryDTOList;
    }

    /**
     *  根据id删除书籍分类
     * @param id
     */
    @Override
    public void deleteBookCategoryById(Integer id) {
        // 1.查询分类下的书籍数量
        Integer bookCount = bookCategoryMapper.getBookCategoryCountById(id);
        if (bookCount > 0) {
            throw new RuntimeException("分类下有书籍，无法删除");
        }
        // 2.如果分类下有书籍，抛出异常
        bookCategoryMapper.deleteBookCategoryById(id);
    }

    @Override
    public void updateBookCategoryById(BookCategory bookCategory) {
        // 直接调用mapper动态更新
        bookCategoryMapper.updateBookCategoryById(bookCategory);

    }
}
