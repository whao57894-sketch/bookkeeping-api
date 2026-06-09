package com.bookkeeping.bookkeepingapi.service;

import com.bookkeeping.bookkeepingapi.dto.LoginRequest;
import com.bookkeeping.bookkeepingapi.dto.RegisterRequest;
import com.bookkeeping.bookkeepingapi.dto.UpdateUserRequest;
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
        if (StringUtils.isAnyBlank(
                request.getPhone(),
                request.getPassword(),
                request.getConfirmPassword(),
                request.getName(),
                request.getOccupation(),
                request.getGender()
        ) || request.getAge() == null) {
            throw new IllegalArgumentException("请完整填写注册信息");
        }
        if (!request.getPhone().matches("^1\\d{10}$")) {
            throw new IllegalArgumentException("请输入正确的手机号码");
        }
        if (request.getAge() <= 0 || request.getAge() > 120) {
            throw new IllegalArgumentException("请输入正确的年龄");
        }
        if (!StringUtils.equals(request.getPassword(), request.getConfirmPassword())) {
            throw new IllegalArgumentException("两次输入的密码不一致");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("手机号已注册");
        }

        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setOccupation(request.getOccupation());
        user.setGender(request.getGender());
        user.setCreatedAt(LocalDateTime.now());

        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse login(LoginRequest request) {
        if (StringUtils.isAnyBlank(request.getPhone(), request.getPassword())) {
            throw new IllegalArgumentException("请输入手机号码和密码");
        }

        User user = userRepository.findByPhone(request.getPhone())
                .filter(item -> StringUtils.equals(item.getPassword(), request.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("手机号或密码错误"));

        return UserResponse.from(user);
    }

    public UserResponse getByPhone(String phone) {
        return UserResponse.from(findUserByPhone(phone));
    }

    public UserResponse update(String phone, UpdateUserRequest request) {
        if (StringUtils.isAnyBlank(request.getName(), request.getOccupation(), request.getGender()) || request.getAge() == null) {
            throw new IllegalArgumentException("请完整填写个人信息");
        }
        if (request.getAge() <= 0 || request.getAge() > 120) {
            throw new IllegalArgumentException("请输入正确的年龄");
        }

        User user = findUserByPhone(phone);
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setOccupation(request.getOccupation());
        user.setGender(request.getGender());
        return UserResponse.from(userRepository.save(user));
    }

    private User findUserByPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new IllegalArgumentException("手机号不能为空");
        }
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }
}
