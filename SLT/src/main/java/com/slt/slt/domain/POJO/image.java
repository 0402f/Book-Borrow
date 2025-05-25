package com.slt.slt.domain.POJO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // 无参构造
@AllArgsConstructor //  全参构造
public class image {
    private int id;
    private String img;
}
