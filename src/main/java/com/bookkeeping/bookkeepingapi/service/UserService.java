package com.bookkeeping.bookkeepingapi.service;

import com.bookkeeping.bookkeepingapi.dto.LoginRequest;
import com.bookkeeping.bookkeepingapi.dto.RegisterRequest;
import com.bookkeeping.bookkeepingapi.dto.UserResponse;
import com.bookkeeping.bookkeepingapi.entity.User;
import com.bookkeeping.bookkeepingapi.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse register(RegisterRequest request) {
        if (StringUtils.isAnyBlank(request.getUsername(), request.getEmail(), request.getPassword(), request.getConfirmPassword())) {
            throw new IllegalArgumentException("用户名、邮箱和密码不能为空");
        }
        if (!StringUtils.equals(request.getPassword(), request.getConfirmPassword())) {
            throw new IllegalArgumentException("两次输入的密码不一致");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCreatedAt(LocalDateTime.now());

        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse login(LoginRequest request) {
        if (StringUtils.isAnyBlank(request.getUsername(), request.getPassword())) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .filter(item -> StringUtils.equals(item.getPassword(), request.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));

        return UserResponse.from(user);
    }
}
