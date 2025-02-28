package com.example.crash.controller;

import com.example.crash.model.user.User;
import com.example.crash.model.user.UserSignUpRequestBody;
import com.example.crash.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    //RequestBody = JSON 형태로 데이터를 받겠다는 의미
    // 회원가입
    @PostMapping
    public ResponseEntity<User> signUp(
            @Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody) {
        var user = userService.signUp(userSignUpRequestBody);
        return ResponseEntity.ok(user);
    }

}
