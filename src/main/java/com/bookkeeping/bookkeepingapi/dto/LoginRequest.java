package com.bookkeeping.bookkeepingapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String phone;
    private String password;
}
