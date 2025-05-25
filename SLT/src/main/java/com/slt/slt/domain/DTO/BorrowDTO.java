package com.slt.slt.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowDTO {
    // 借阅编号
    private String borrowIsn;
    // 借阅id
    private Integer borrowId;
    // 用户id
    private Integer readerId;
    // 书籍id
    private Integer bookId;
    // 借阅日期
    private LocalDateTime borrowDate;
    // 归还日期
    private LocalDateTime returnDate;
    // 应还日期
    private LocalDateTime dueDate;
    // 借阅状态
    private Integer status;
    // 借阅天数
    private Integer days;
    // 借阅数量
    private Integer counts;
    // 归还的数量
    private Integer returnCounts;
    // 书籍名称
    private String title;
    // 书籍封面图片
    private String imageUrl;


}
