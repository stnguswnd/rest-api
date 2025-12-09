package com.example.restapi.service;

import com.example.restapi.dto.response.UserResponse;

public interface UserService {
    UserResponse findByUsername(String username);
}