package com.slt.slt.mapper;


import com.slt.slt.domain.DTO.BookCategoryDTO;
import com.slt.slt.domain.POJO.BookCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookCategoryMapper {
    /**
     * 获取分类列表
     * @return
     */
    @Select("select * from book_category")
    List<BookCategoryDTO> getBookCategoryList();

    /**
     *  更据分类id获取分类下的书籍种类数量
     * @param id
     * @return
     */
    int getBookCategoryCountById(int id);

    /**
     *  新增书籍分类
     * @param bookCategory
     */
    @Insert("insert into book_category (name, des) values (#{name}, #{des})")
    void save(BookCategory bookCategory);

    @Select("select count(*) from book_category")
    Long getBookCategoryCount();
    @Select("select * from book_category")
    List<BookCategory> getCategoryList();

    /**
     *  根据id删除书籍分类
     * @param id
     */
    @Delete("delete from book_category where id = #{id}")
    void deleteBookCategoryById(Integer id);

    /**
     *  根据分类名称获取分类信息
     * @param name
     * @return
     */
    @Select("select * from book_category where name = #{name}")
    BookCategory getBookCategoryByName(String name);

    /**
     * 更据id来更新书籍分类信息
     * @param bookCategory
     */
    void updateBookCategoryById(BookCategory bookCategory);

    /**
     *  更据分类名模糊查询
     * @param name
     * @return
     */
    List<BookCategory> getBookCategoryByLikeName(String name);
}
