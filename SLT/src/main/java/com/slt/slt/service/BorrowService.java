package com.slt.slt.service;

import com.slt.slt.domain.DTO.BorrowDTO;
import com.slt.slt.domain.POJO.Borrow;
import com.slt.slt.domain.VO.BorrowVO;

import java.util.List;

public interface BorrowService {
    // 借阅书籍
    void BorrowBook(BorrowVO borrowVO);

    /**
     * 获取借阅记录
     * @param userId
     * @return
     */
    List<BorrowDTO> getBorrowList(Integer userId);

    /**
     * 删除借阅记录
     * @param borrowId
     */
    void deleteBorrow(Integer borrowId);

    /**
     *  更据id查询借阅记录
     * @param borrowId
     * @return
     */
    Borrow getBorrowById(Integer borrowId);

    void ReturnBook(Integer borrowId,Integer returnCounts);
}
