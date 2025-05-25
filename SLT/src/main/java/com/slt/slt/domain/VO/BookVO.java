package com.slt.slt.domain.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * BookVO用于接受前端传递的参数
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookVO {
    private Integer id;
    // 书名
    private String title;
    // 作者
    private String author;
    // 出版社
    private String publisher;
    // 出版日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;
    // 分类id
    private Integer categoryId;
    // BookVO.java
    private String isbn; // 新增字段
    // 库存
    private int stock;
    // 封面图片
    private String imageUrl;
    // 描述
    private String des;
    // 总数
    private int total;
}
