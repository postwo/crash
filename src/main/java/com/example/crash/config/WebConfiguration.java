package com.example.crash.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http    .cors(Customizer.withDefaults()) // cors는 기본값으로 동작

                .authorizeHttpRequests(
                        (requests) ->
                                requests
                                        .anyRequest()
                                        .permitAll()

                                        )
                .sessionManagement( //RESTful API에서는 보통 세션을 생성하지 않는 것이 일반적 , 세션 생성 x
                        (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(CsrfConfigurer::disable) //csrf 설정 필요없다 그러므로 꺼준다
                .httpBasic(HttpBasicConfigurer::disable); // 시큐리티에서 기본적으로 활성화시켜주는 basic도 꺼준다

        return http.build();
    }
}
