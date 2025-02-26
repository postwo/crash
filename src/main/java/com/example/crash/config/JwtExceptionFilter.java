package com.example.crash.config;

import com.example.crash.model.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//JwtAuthenticationFilter(jwt검증필터이다) 실행 중에 JWT 예외가 발생하면, 이 필터가 예외를 처리한다
@Component
public class JwtExceptionFilter extends OncePerRequestFilter { // JWT 검증 과정 중 예외가 발생

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // exception 이 발생하지 않으면 WebConfiguration에 .addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter.getClass())  이게 동작을한다
            // .addFilterBefore을 통해 jwtExceptionFilter에 exception이 발생하지 않으면 jwtAuthenticationFilter.getClass() 다음필터인 이게 동작한다
            filterChain.doFilter(request, response);
        } catch (JwtException exception) {
            //JWT 검증 중 예외가 발생하면 예외 처리
            response.setContentType(MediaType.APPLICATION_JSON_VALUE); //클라이언트에게 반환할 응답의 내용 타입을 JSON 형식으로 설정
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //상태 코드를 401 Unauthorized로 설정
            response.setCharacterEncoding("UTF-8"); //응답의 문자 인코딩을 UTF-8로 설정
            var errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());

            //JSON으로 직렬화한다는 것은 객체를 JSON 형식의 문자열로 변환하는 것을 의미
            //예외 정보를 JSON 형식으로 변환하여 응답 본문에 작성
            ObjectMapper objectMapper = new ObjectMapper(); //ObjectMapper를 생성하여 자바 객체를 JSON 문자열로 변환
            String responseJson = objectMapper.writeValueAsString(errorResponse); //writeValueAsString: 주어진 객체를 JSON 문자열로 변환하는 메서드
            response.getWriter().write(responseJson);//변환된 JSON 문자열을 응답 본문에 쓴다
        }
    }
}
