package com.slt.slt.domain.POJO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    // 主键
    private Integer id;
    // 书名
    private String title;
    // 作者
    private String author;
    // 出版社
    private String publisher;
    // 价格
    private String isbn;
    // 出版日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;
    // 分类id
    private Integer categoryId;
    // 库存
    private int stock;
    // 封面图片
    private String imageUrl;
    // 描述
    private String des;
    // 总数
    private int total;
    // 状态
    private Integer status;
}
