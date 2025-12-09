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

    //유효한지에 대한 정보와 사용자에 대한 정보를 Object 객체로 전달함.
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> validateToken(@RequestParam String token){
        boolean valid = jwtProvider.validateToken(token);
        String username = valid ? jwtProvider.getUsername(token) : null;

        return ResponseEntity.ok(ApiResponse.success(
                Map.of(
                        "valid", valid,
                        "username", username
                )
        ));
    }

}
