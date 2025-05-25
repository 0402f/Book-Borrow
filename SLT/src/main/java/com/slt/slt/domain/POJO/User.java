package com.slt.slt.domain.POJO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // 无参构造
@AllArgsConstructor //  全参构造
public class User {
    private Integer id;
    private String userNumber;
    private String password;
    private String avtor;
    private String iphone;
    private String nickName;

}
