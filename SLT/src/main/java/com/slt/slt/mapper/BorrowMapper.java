package com.slt.slt.mapper;


import com.slt.slt.domain.DTO.BorrowDTO;
import com.slt.slt.domain.POJO.Borrow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BorrowMapper {
    void BorrowBook(Borrow borrow);

    /**
     *  更据用户ID获取用户借阅书籍的总数量
     * @param readerId
     * @return
     */

    int GetBorrowCountByReaderId(Integer readerId);

    /**
     * 更据用户ID和书籍ID获取用户借阅某种书籍的总数量
     * @param bookId
     * @param readerId
     * @return
     */
    int GetBorrowCountByBookId(Integer bookId, Integer readerId);

    /**
     *  获取用户借阅记录
     * @param userId
     * @return
     */
    List<BorrowDTO> GetBorrowList(Integer userId);

    /**
     *  更具借阅id获取借阅记录
     * @param borrowId
     * @return
     */
    @Select("select * from borrow where borrow_id = #{borrowId}")
    Borrow GetBorrowById(Integer borrowId);

    /**
     *  删除借阅记录
     * @param borrowId
     */
    @Update("update borrow set dele = 1 where borrow_id = #{borrowId}")
    void DeleteBorrow(Integer borrowId);

    /**
     * 归还书籍
     * @param borrow
     */
    void UpdateBorrow(Borrow borrow);
}
