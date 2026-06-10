package com.bookkeeping.bookkeepingapi.controller;

import com.bookkeeping.bookkeepingapi.dto.ApiResponse;
import com.bookkeeping.bookkeepingapi.dto.LoginRequest;
import com.bookkeeping.bookkeepingapi.dto.RegisterRequest;
import com.bookkeeping.bookkeepingapi.dto.UpdateUserRequest;
import com.bookkeeping.bookkeepingapi.dto.UserResponse;
import com.bookkeeping.bookkeepingapi.service.BookkeepingService;
import com.bookkeeping.bookkeepingapi.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final BookkeepingService bookkeepingService;

    public UserController(UserService userService, BookkeepingService bookkeepingService) {
        this.userService = userService;
        this.bookkeepingService = bookkeepingService;
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

    @GetMapping("/{phone}")
    public ApiResponse<UserResponse> getUser(@PathVariable String phone) {
        try {
            return ApiResponse.ok("查询成功", userService.getByPhone(phone));
        } catch (IllegalArgumentException ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }

    @PutMapping("/{phone}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String phone, @RequestBody UpdateUserRequest request) {
        try {
            return ApiResponse.ok("保存成功", userService.update(phone, request));
        } catch (IllegalArgumentException ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }

    @DeleteMapping("/{phone}")
    public ApiResponse<Void> deleteUser(@PathVariable String phone) {
        try {
            userService.delete(phone);
            return ApiResponse.ok("\u6ce8\u9500\u6210\u529f", null);
        } catch (IllegalArgumentException ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }

    @GetMapping("/{phone}/bookkeeping-count")
    public ApiResponse<Integer> getBookkeepingCount(@PathVariable String phone) {
        try {
            userService.getByPhone(phone);
            return ApiResponse.ok("查询成功", (int) bookkeepingService.countByPhone(phone));
        } catch (IllegalArgumentException ex) {
            return ApiResponse.fail(ex.getMessage());
        }
    }
}
