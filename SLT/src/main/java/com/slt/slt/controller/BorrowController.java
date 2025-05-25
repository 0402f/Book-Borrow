package com.slt.slt.controller;


import com.slt.slt.Result.Result;
import com.slt.slt.domain.DTO.BorrowDTO;
import com.slt.slt.domain.POJO.Borrow;
import com.slt.slt.domain.VO.BorrowVO;
import com.slt.slt.service.BorrowService;
import com.slt.slt.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *  图书借阅管理
 */
@RestController
@Slf4j
@RequestMapping("/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    /**
     *  图书借阅
     * @param borrowVO
     * @return
     */
    @PostMapping("/BorrowBook")
    public Result<String> BorrowBook(@RequestBody BorrowVO borrowVO) {

        // 获取当前登录用户的id
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        borrowVO.setReaderId(userId);
        log.info("图书借阅: {}", borrowVO);
        if(borrowVO == null){
            return Result.error("借阅信息不能为空");
        }
        borrowService.BorrowBook(borrowVO);
        return Result.success("借阅成功");
    }

    /**
     * 查询历史借阅记录
     * @return
     */
    @GetMapping("/getBorrowList")
    public Result<List<BorrowDTO>>  getBorrowList() {
        // 获取当前登录用户的id
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        log.info("查询借阅记录: {}", userId);
        List<BorrowDTO> borrowList = borrowService.getBorrowList(userId);
        return Result.success(borrowList);
    }

    /**
     *  删除借阅记录
     * @param borrowId
     * @return
     */
    @PostMapping("/deleteBorrow")
    public Result<String> deleteBorrow( Integer borrowId) {
        log.info("删除借阅记录: {}", borrowId);
        borrowService.deleteBorrow(borrowId);
        return Result.success("删除成功");
    }

    /**
     * 更据id查询借阅记录
     * @param borrowId
     * @return
     */
    @GetMapping("/getBorrowById")
    public Result<Borrow> getBorrowById(Integer borrowId) {
        log.info("查询借阅记录: {}", borrowId);
        Borrow borrow = borrowService.getBorrowById(borrowId);
        return Result.success(borrow);
    }

    /**
     *  归还书籍
     * @param borrowId
     * @return
     */
    @PostMapping("/ReturnBook")
    public Result<String> ReturnBook( Integer borrowId, Integer returnCounts) {
        log.info("归还书籍: {},{}", borrowId,returnCounts);
        borrowService.ReturnBook(borrowId,returnCounts);
        return Result.success("归还成功");
    }


}
