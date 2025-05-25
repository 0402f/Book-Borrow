package com.slt.slt.controller;

import com.slt.slt.Result.Result;
import com.slt.slt.domain.POJO.Book;
import com.slt.slt.domain.VO.BookVO;
import com.slt.slt.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    /**
     *  获取书籍所有数量
     * @return
     */
    @GetMapping("/Count")
    public Result<Long> GetBookCount() {
        return Result.success(bookService.GetBookCount());
    }

    /**
     *  根据分类id获取书籍列表
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Book>>   GetBookListByCategoryId(Integer categoryId,Integer status){
        log.info("根据分类id获取书籍列表: categoryId={},status={}", categoryId,status);
        if(categoryId == null){
            return Result.error("分类id不能为空");
        }
        List<Book> list =  bookService.GetBookListByCategoryId(categoryId,status);
        return Result.success(list);
    }

    /**
     *  新增书籍
     * @param bookVO
     * @return
     */
    @PostMapping("/save")
    public Result<String> SaveBook(@RequestBody BookVO bookVO){
        log.info("新增书籍: {}",  bookVO.toString());
        if(bookVO == null){
            return Result.error("书籍信息不能为空");
        }
        bookService.SaveBook(bookVO);
        return Result.success("成功");
    }

    /**
     * 修改书籍信息
     * @param bookVO
     * @return
     */
    @PostMapping("/update")
    public Result<String> UpdateBook(@RequestBody BookVO bookVO){
        log.info("更新书籍: {}",  bookVO);
        if(bookVO == null){
            return Result.error("书籍信息不能为空");
        }
        bookService.UpdateBook(bookVO);
        return Result.success("成功");
    }

    /**
     * 上,下架书籍
     * @param id
     * @param status 1:上架 0:下架
     * @return
     */
    @PostMapping("/UporDown")
    public Result<String> UpOrDownBook(Integer id,Integer status){
        log.info("修改书籍: {}",  id);
        if(id == null){
            return Result.error("书籍id不能为空");
        }
        bookService.UpOrDownBook(id,status);
        return Result.success("操作成功");
    }

    /**
     *  更据书籍名或者分类名模糊查询
     * @param name
     * @return
     */
    @GetMapping("/GetBookByLikeName")
    public Result<List<Book>> GetBookByLikeName(String name){
        log.info("根据名称模糊查询书籍: {}",  name);
        if(name == null){
            return Result.error("书籍名称不能为空");
        }
        List<Book> list = bookService.GetBookByLikeName(name);
        return Result.success(list);

    }

    /**
     *  更据id获取书籍信息
     * @param id
     * @return
     */
    @GetMapping("/GetBookById")
    public Result<Book> GetBookById(Integer id){
        log.info("根据id查询书籍: {}",  id);
        if(id == null){
            return Result.error("书籍id不能为空");
        }
        Book book = bookService.GetBookById(id);
        return Result.success(book);
    }



}
