package com.slt.slt.service.serviceImpl;

import com.slt.slt.domain.POJO.Book;
import com.slt.slt.domain.POJO.BookCategory;
import com.slt.slt.domain.VO.BookVO;
import com.slt.slt.mapper.BookCategoryMapper;
import com.slt.slt.mapper.BookMapper;
import com.slt.slt.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl  implements BookService {


    private final BookMapper bookMapper;
    private final BookCategoryMapper bookCategoryMapper;

    /**
     *  获取书籍数量
     * @return
     */
    @Override
    public Long GetBookCount() {
        return bookMapper.GetBookCount();
    }

    /**
     *  根据分类id获取书籍列表
     * @param categoryId
     * @return
     */
    @Override
    public List<Book> GetBookListByCategoryId(Integer categoryId,Integer status) {
        List<Book> list = bookMapper.GetBookListByCategoryId(categoryId,status);
        return list;
    }

    /**
     *  新增书籍
     * @param bookVO
     */
    @Override
    public void SaveBook(BookVO bookVO) {
        // 不用检验树是否存在，因为有的书是相同名称的，但是不同的作者
        // 创建一个Book对象
        Book book = new Book();
        // 将BookVO中的属性复制到Book对象中
        BeanUtils.copyProperties(bookVO, book);
        // 为新增的数据添加status
        book.setStatus(1);
        // 获取BookVO中书籍的总数赋值给库存
        book.setStock(bookVO.getTotal());
        // 插入数据库
        bookMapper.SaveBook(book);
    }

    /**
     *  更新书籍信息
     * @param bookVO
     */
    @Override
    public void UpdateBook(BookVO bookVO) {
        Book book1 = new Book();
        BeanUtils.copyProperties(bookVO, book1);
        // 先更据id查询当前书籍的信息
        Book book = bookMapper.GetBookById(bookVO.getId());
        // 检验当前书籍的总数量与最新的总数量是否相同
        int total = book.getTotal();
        // 更据差值来更新库存 库存+差值
        int number = bookVO.getTotal() - total; // 当前值
        if (number > 0){
            // 大于0 说明是增加
        }else{
            // 小于0 说明是减少
            if(book.getStock() < Math.abs(number)){
                // 库存小于减少的值
                throw new RuntimeException("减少数量不能大于库存");
            }
        }
        // 更新库存
        book1.setStock(book.getStock() + number);
        bookMapper.UpdateBook(book1);

    }

    /**
     * 上架,下架图书
     * @param id
     */
    @Override
    public void UpOrDownBook(Integer id,Integer status) {
        // 更据id查询当前图书信息
        Book book = bookMapper.GetBookById(id);

        if(book == null){
            throw new RuntimeException("图书不存在");
        }
        if(status == 0){
            // 表示目标是下架
            if(book.getStock() != book.getTotal()){
                throw new RuntimeException("图书未全部归还,不可下架");
            }
            // 下架
            book.setStatus(0);
        }else{
            // 表示目标是上架,直接上架
            book.setStatus(1);
        }
        bookMapper.UpdateBook(book);
    }

    /**
     * 根据书籍名或者分类名模糊查询
     * @param name
     * @return
     */
    @Override
    public List<Book> GetBookByLikeName(String name) {
        // 1.直接从书籍表中名字莫模糊查询
        List<Book> list = bookMapper.GetBookByLikeName(name);
        // 2.从分类表中模糊查询得到分类id,再从书籍表中更据id来获得书籍信息
        List<BookCategory> categories = bookCategoryMapper.getBookCategoryByLikeName(name);
        List<Book> BookCategoryList = new ArrayList<>();
        for(BookCategory bookCategory : categories){
            int categoryId = bookCategory.getId();
            BookCategoryList.addAll(bookMapper.GetBookListByCategoryId(categoryId,1));
        }

        Set<Integer> booksIds = new HashSet<>();
        List<Book> result = new ArrayList<>();
        for(Book book : list){
            if(booksIds.add(book.getId())){
                result.add(book);
            }
        }
        for(Book book : BookCategoryList){
            if(booksIds.add(book.getId())){
                result.add(book);
            }
        }

        return result;
    }

    /**
     *  更据id获取书籍信息
     * @param id
     * @return
     */
    @Override
    public Book GetBookById(Integer id) {
        Book book = bookMapper.GetBookById(id);
        return book;
    }
}