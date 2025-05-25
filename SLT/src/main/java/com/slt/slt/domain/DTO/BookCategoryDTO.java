package com.slt.slt.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用来给前端返回的分类列表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoryDTO {
    // 分类id
    private int id;
    // 分类名称
    private String name;
    // 分类描述
    private String des;
    // 分类下的图书数量
    private Integer bookCount;
}
