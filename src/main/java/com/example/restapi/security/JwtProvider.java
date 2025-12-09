package com.example.restapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretString;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // HS256
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    // JWT 토큰 발행
    public String createToken(String username){

        Date now = new Date();
        Date expirayDate = new Date(now.getTime() + expiration);


        return Jwts.builder().subject(username).issuedAt(now).expiration(expirayDate).signWith(secretKey).compact();
    }

    // claims 검증, 복호화 하는 과정
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //토큰에서 사용자명 추출
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원하지 않는 JWT입니다.");
        } catch (MalformedJwtException e) {
            System.out.println("잘못된 JWT 입니다.");
        } catch (SignatureException e) {
            System.out.println("서명이 잘못되었습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("토큰이 없습니다.");
        }
        return false;
    }


}
