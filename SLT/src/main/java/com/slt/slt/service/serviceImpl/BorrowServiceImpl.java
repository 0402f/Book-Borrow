package com.slt.slt.service.serviceImpl;

import com.slt.slt.domain.DTO.BorrowDTO;
import com.slt.slt.domain.POJO.Book;
import com.slt.slt.domain.POJO.Borrow;
import com.slt.slt.domain.VO.BorrowVO;
import com.slt.slt.mapper.BookMapper;
import com.slt.slt.mapper.BorrowMapper;
import com.slt.slt.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class BorrowServiceImpl implements BorrowService {

    private final BorrowMapper borrowMapper;
    private final BookMapper  bookMapper;
    /**
     * 借阅书籍
     * @param borrowVO
     */
    @Override
    @Transactional
    public void BorrowBook(BorrowVO borrowVO) {
        // 1. 检查书籍是否存在
        Book book = bookMapper.GetBookById(borrowVO.getBookId());
        if (book == null) {
            throw new RuntimeException("书籍不存在");
        }
        // 2. 检查书籍是否可用
        if (book.getStock() <= 0) {
            throw new RuntimeException("当前书籍库存不足");
        }
        // 检查当前用户借阅的书籍数量是否超过限制
        int borrowCount = borrowMapper.GetBorrowCountByReaderId(borrowVO.getReaderId());
        // 记录剩余可借阅的书籍数量
        int remainCount = 20 - borrowCount;
        // 加上当前借阅的书籍数量
        borrowCount += borrowVO.getCounts();
        if (borrowCount > 20) {
            throw new RuntimeException("您已借阅的书籍数量超过限制"+"可借阅的书籍数量为："+remainCount+"本");
        }
        // 检查当前用户借阅该书籍的数量是否超过限制
        int borrowCountByBookId = borrowMapper.GetBorrowCountByBookId(borrowVO.getBookId(),borrowVO.getReaderId());
        // 记录剩余可借阅的书籍数量
        int remainCountByBookId = 5 - borrowCountByBookId;
        // 加上当前借阅的书籍数量
        borrowCountByBookId += borrowVO.getCounts();
        if (borrowCountByBookId > 5) {
            throw new RuntimeException("(超过限制)该书可借："+remainCountByBookId+"本");
        }
        // 3.借阅书籍
        // (1). 减少书籍库存
        bookMapper.UpdateBookStock(borrowVO.getBookId(),borrowVO.getCounts());
        // (2). 增加借阅记录
        // 创建一个新的借阅记录
        Borrow borrow = new Borrow();
        // TODO:生成一个唯一的借阅编号
        // 时间戳+随机数
        String borrowIsn = System.currentTimeMillis() + "" + (int) (Math.random() * 100000);
        borrow.setBorrowIsn(borrowIsn);
        // 将已有的借阅信息复制到新的借阅记录中
        BeanUtils.copyProperties(borrowVO, borrow);
        // 设置借阅日期为当前系统时间 时间精确到秒,为当前系统时间的时分秒
        borrow.setBorrowDate(LocalDateTime.now());
        // 归还日期不用设置，应为刚借阅，还未归还
        borrow.setReturnDate(null);
        // 设置应还日期为借阅日期加上借阅天数
        borrow.setDueDate(borrow.getBorrowDate().plusDays(borrowVO.getDays()));
        // 设置借阅状态为借阅中
        borrow.setStatus(2);
        // 设置归还书籍数量为0
        borrow.setReturnCounts(0);
        // 设置默认不删除记录
        borrow.setDele(0);
        // 插入借阅记录
        borrowMapper.BorrowBook(borrow);



    }

    /**
     * 获取借阅记录
     * @param userId
     * @return
     */
    @Override
    public List<BorrowDTO> getBorrowList(Integer userId) {
        List<BorrowDTO> list =  borrowMapper.GetBorrowList(userId);
        return list;
    }

    /**
     * 删除借阅记录
     * @param borrowId
     */
    @Override
    public void deleteBorrow(Integer borrowId) {
        // 1.查询借阅记录
        Borrow borrow = borrowMapper.GetBorrowById(borrowId);
        // 2.检查借阅记录是否存在
        if (borrow == null) {
            throw new RuntimeException("借阅记录不存在");
        }
        // 如果没有归还书籍就不能删除借阅记录
        if (borrow.getStatus() != 1) {
            throw new RuntimeException("未归还书籍不能删除借阅记录");
        }
        // 3.删除借阅记录-->实际上是将is_show设置为0
        borrowMapper.DeleteBorrow(borrowId);

    }

    /**
     * 更据id查询借阅记录
     * @param borrowId
     * @return
     */
    @Override
    public Borrow getBorrowById(Integer borrowId) {
        Borrow borrow = borrowMapper.GetBorrowById(borrowId);
        return borrow;
    }

    /**
     *  归还书籍
     * @param borrowId
     * @param returnCounts
     */
    @Override
    @Transactional // 添加事务注解 保证数据的一致性
    public void ReturnBook(Integer borrowId, Integer returnCounts) {
        // 1.查询借阅记录
        Borrow borrow = borrowMapper.GetBorrowById(borrowId);
        if (borrow == null) {
            throw new RuntimeException("借阅记录不存在");
        }
        
        // 检查归还数量是否合法
        if (returnCounts <= 0) {
            throw new RuntimeException("归还数量必须大于0");
        }
        
        // 计算当前已归还数量
        int totalReturnCounts = borrow.getReturnCounts() + returnCounts;
        
        // 检查是否超出可归还数量
        if (totalReturnCounts > borrow.getCounts()) {
            throw new RuntimeException("归还数量超出借阅数量");
        }
        
        // 设置归还书籍数量
        borrow.setReturnCounts(totalReturnCounts);
        
        // 判断是否全部归还
        if (borrow.getCounts().equals(totalReturnCounts)) {
            borrow.setStatus(1);
            borrow.setReturnDate(LocalDateTime.now());
        }
        
        // 更新借阅记录
        borrowMapper.UpdateBorrow(borrow);
        // 更新书籍库存
        bookMapper.UpdateBookStock(borrow.getBookId(),-returnCounts);
    }
}
