package com.example.restapi.service;

import com.example.restapi.dto.request.LoginRequest;
import com.example.restapi.dto.request.SignupRequest;
import com.example.restapi.dto.response.TokenResponse;
import com.example.restapi.dto.response.UserResponse;
import com.example.restapi.entity.User;
import com.example.restapi.exception.CustomException;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.repository.UserRepository;
import com.example.restapi.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public UserResponse signUp(SignupRequest request){
        // 중복 검사
        if (userRepository.existsByUsername(request.getUsername())){
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .name(request.getName())
                .email(request.getEmail())
                .build();
        User saved = userRepository.save(user); //여기까지는 엔티티 객체이기 때문에 Dto 객체로 변환
        return UserResponse.from(saved);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);

        }

        String accessToken = jwtProvider.createToken(user.getUsername());

        return TokenResponse.of(accessToken);

    }

}
