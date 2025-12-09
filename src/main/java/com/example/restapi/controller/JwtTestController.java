package com.example.restapi.controller;

import com.example.restapi.dto.response.ApiResponse;
import com.example.restapi.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/jwt-test")
@RequiredArgsConstructor
public class JwtTestController {

    private final JwtProvider jwtProvider;

    @GetMapping("/generate")
    public ResponseEntity<ApiResponse<Map<String, String>>> generateToken(
        @RequestParam String username
    ){
        String token = jwtProvider.createToken(username);
        return ResponseEntity.ok(ApiResponse.success(Map.of("token", token)));
    }

    //@GetMapping("/validate")

}
