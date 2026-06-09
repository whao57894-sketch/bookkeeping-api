package com.bookkeeping.bookkeepingapi.dto;

import com.bookkeeping.bookkeepingapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String phone;
    private String name;
    private Integer age;
    private String occupation;
    private String gender;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getPhone(),
                user.getName(),
                user.getAge(),
                user.getOccupation(),
                user.getGender()
        );
    }
}
