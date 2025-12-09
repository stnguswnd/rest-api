package com.example.restapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private String tokenType;

    // of와 from 차이가 뭐지?
    public static TokenResponse of(String accessToken){
        return new TokenResponse(accessToken, "Bearer");
    }
}
