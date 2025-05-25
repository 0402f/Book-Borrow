package com.slt.slt.domain.DTO;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {
    private String userNumber;
    private String password;

}
