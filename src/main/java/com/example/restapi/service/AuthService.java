package com.example.restapi.service;

import com.example.restapi.dto.request.LoginRequest;
import com.example.restapi.dto.request.SignupRequest;
import com.example.restapi.dto.response.TokenResponse;
import com.example.restapi.dto.response.UserResponse;

public interface AuthService {
    UserResponse signUp(SignupRequest request); //아이디 패스워드, 네임 , 유저네임
    TokenResponse login(LoginRequest request); //아이디 패스워드만
}
