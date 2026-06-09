package com.bookkeeping.bookkeepingapi.controller;

import com.bookkeeping.bookkeepingapi.dto.ApiResponse;
import com.bookkeeping.bookkeepingapi.dto.LoginRequest;
import com.bookkeeping.bookkeepingapi.dto.RegisterRequest;
import com.bookkeeping.bookkeepingapi.dto.UserResponse;
import com.bookkeeping.bookkeepingapi.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody RegisterRequest request) {
        try {
            return ApiResponse.ok("注册成功", userService.register(request));
        } catch (IllegalArgumentException ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@RequestBody LoginRequest request) {
        try {
            return ApiResponse.ok("登录成功", userService.login(request));
        } catch (IllegalArgumentException ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }
}
