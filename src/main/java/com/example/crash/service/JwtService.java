package com.example.crash.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private final SecretKey key;

    //로그인 성공 시 이 키를 사용하여 JWT를 서명하고 발급
    public JwtService(@Value("${jwt.secret-key}") String key) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key)); //encode 되어있는 시크릿키를 다시 BASE64 로 decode 를 한다
    }

    //UserDetails 객체에서 username을 추출하여 JWT 액세스 토큰을 생성하는 역할
    // 사용자 인증을 통해 인증을 완료된 유저의 username을 subject로 대입해서 토큰을 만들어 accesstoken이라는 형태로 제공함수를 만든다
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    // 그리고 그렇게 생성된 accessToken으로부터 다시 username을 추출해서 전달해주는 getUsername 함수 생성
    public String getUsername(String accessToken) {
        return getSubject(accessToken);
    }

    // jwt 생성
    private String generateToken(String subject) { //토큰에 담을 subject를 담는다
        var now = new Date();
        var exp = new Date(now.getTime() + (1000 * 60 * 60 * 3)); // 현재 시간에서 3시간 뒤 만료
        return Jwts.builder().subject(subject).signWith(key)
                .issuedAt(now) //토큰 발행시점 현재
                .expiration(exp) //만료 시점은 현재로 부터 3시간 뒤
                .compact();
    }


    //jwt 에서 subject를 추출하는 함수
    private String getSubject(String token) {
        try {
            return Jwts.parser()
                    //서버에 설정된 비밀 키를 사용하여 JWT 토큰의 서명이 유효한지 확인하는 과정입니다. 이렇게 하면 클라이언트가 보낸 토큰이 서버에서 생성된 토큰과 동일한지, 그리고 변조되지 않았는지를 검증
                    .verifyWith(key) // key값으로 먼저 검증 ,서명은 JWT 토큰의 헤더(header)와 페이로드(payload)에 대한 해시 값입니다.이 해시 값은 서버의 비밀 키(secret key)로 서명되어 있다
                    .build()
                    // 사용자 이름(subject)을 추출하여 해당 사용자를 식별
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException exception) { //검증 실패시 처리
            logger.error("JwtException", exception);
            throw exception;
        }
    }
}
