package com.slt.slt.mapper;


import com.slt.slt.domain.POJO.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BookMapper {
    /**
     * 获取所有书籍数量
     * @return
     */
    @Select("SELECT SUM(total) FROM books WHERE status = 1;")
    Long GetBookCount();

    /**
     *  根据分类id获取书籍列表
     * @param categoryId
     * @return
     */
    List<Book> GetBookListByCategoryId(Integer categoryId,Integer status);

    /**
     *  新增书籍
     * @param book
     */
    void SaveBook(Book book);

    /**
     * 更据id获取书籍信息
     * @param id
     * @return
     */
    @Select("SELECT * FROM books WHERE id = #{id};")
    Book GetBookById(Integer id);

    /**
     *  更新书籍信息
     * @param book
     */
    void UpdateBook(Book book);

    /**
     *  更据书籍名模糊查询
     * @param name
     * @return
     */
    @Select("SELECT * FROM books WHERE title LIKE CONCAT('%', #{name}, '%') AND status = 1;")
    List<Book> GetBookByLikeName(String name);

    /**
     * 更据书籍id更新库存
     * @param bookId
     * @param counts
     */
    @Update("UPDATE books SET stock = stock - #{counts} WHERE id = #{bookId};")
    void UpdateBookStock(Integer bookId, Integer counts);
}
