package com.slt.slt.controller;

import com.slt.slt.Result.Result;
import com.slt.slt.Result.ResultPage;
import com.slt.slt.domain.DTO.BookCategoryDTO;
import com.slt.slt.domain.DTO.CategoryDTO;
import com.slt.slt.domain.POJO.BookCategory;
import com.slt.slt.service.BookCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookCategory")
@RequiredArgsConstructor

public class BookCategoryController {

    private final BookCategoryService bookCategoryService;

    /**
     *  分页查询书籍分类列表
     * @param pageNum  页码
     * @param pageSize 每页显示的数量
     * @return
     */
    @GetMapping("/page")
    public Result<ResultPage<BookCategoryDTO>> getBookCategoryPage(Integer pageNum, Integer pageSize){
        log.info("分页查询书籍分类列表: pageNum={}, pageSize={}", pageNum, pageSize);
        ResultPage<BookCategoryDTO> resultPage  = bookCategoryService.getBookCategoryPage(pageNum, pageSize);
        return Result.success(resultPage);
    }

    /**
     * 新增书籍分类
     * @param bookCategory
     * @return
     */
    @PostMapping("/save")
    public Result<String> SaveBookCategory(@RequestBody BookCategory bookCategory){
        log.info("新增书籍分类: {}",  bookCategory);
        bookCategoryService.save(bookCategory);
        return Result.success("新增书籍分类成功");
    }

    /**
     * 查询书籍分类数量
     * @return
     */
    @GetMapping("/count")
    public Result<Long> BookCategoryCount(){
        log.info("查询书籍分类数量");
        Long count = bookCategoryService.getBookCategoryCount();
        return Result.success(count);
    }

    /**
     * 查询分类列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<CategoryDTO>> getCategoryList(){
        log.info("查询分类列表");
        List<CategoryDTO> categoryList = bookCategoryService.getCategoryList();
        return Result.success(categoryList);
    }

    /**
     * 更据Id删除分类
     */
    @DeleteMapping
    public Result<String> DeleteBookCategoryById(Integer id){
        log.info("删除分类: {}", id);
        bookCategoryService.deleteBookCategoryById(id);
        return Result.success("删除分类成功");
    }

    /**
     * 更据Id更新分类
     * @param bookCategory
     * @return
     */
    @PutMapping
    public Result<String> UpdateBookCategoryById(@RequestBody BookCategory bookCategory){
        log.info("更新分类: {}", bookCategory);
        bookCategoryService.updateBookCategoryById(bookCategory);
        return Result.success("更新分类成功");
    }

}
