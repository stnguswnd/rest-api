package com.example.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message= "username은 필수입니다.")
    private String username;

    @NotBlank(message= "password는 필수입니다.")
    private String password;
}
