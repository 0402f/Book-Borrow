package com.slt.slt.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor // 无参构造
@AllArgsConstructor //  全参构造
public class UserVO implements Serializable {

    private Integer id;
    private String userNumber;
    private String password;
}
