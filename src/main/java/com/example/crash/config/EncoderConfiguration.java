package com.example.crash.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class EncoderConfiguration {

    //passwordencoder를 WebConfiguration 에 같이 구현하니까 무한 참조가 발생해서 이렇게 설정
    // 클래스를 따로 구현하므로 에러 해결
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
