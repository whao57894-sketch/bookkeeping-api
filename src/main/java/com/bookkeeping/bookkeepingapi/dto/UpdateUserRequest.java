package com.bookkeeping.bookkeepingapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    private String name;
    private Integer age;
    private String occupation;
    private String gender;
}
