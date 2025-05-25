package com.slt.slt.domain.POJO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 借阅记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Borrow {
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
    private Integer status; // (0:已逾期 1:已归还 2:借阅中)
    // 借阅天数
    private Integer days;
    // 借阅数量
    private Integer counts;
    // 归还的数量
    private Integer returnCounts;
    // 用户是否查看
    private Integer dele; // (0:不删除 1:删除)

}
