package com.slt.slt.domain.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowVO {
    // 借阅id
    private Integer borrowId;
    // 用户id
    private Integer readerId;
    // 书籍id
    private Integer bookId;
    // 借阅状态
    private Integer status;//  (0:已逾期 1:已归还 2:借阅中)
    // 借阅天数
    private Integer days;
    // 借阅数量
    private Integer counts;

}
