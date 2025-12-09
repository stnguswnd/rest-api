package com.example.restapi.controller;


import com.example.restapi.dto.request.LoginRequest;
import com.example.restapi.dto.request.SignupRequest;
import com.example.restapi.dto.response.ApiResponse;
import com.example.restapi.dto.response.TokenResponse;
import com.example.restapi.dto.response.UserResponse;
import com.example.restapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup (
            @Valid @RequestBody SignupRequest request
    ) {
        UserResponse response = authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login (
            @Valid @RequestBody LoginRequest request
    ) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
