package com.example.crash.service;

import com.example.crash.exception.user.UserAlreadyExistsException;
import com.example.crash.exception.user.UserNotFoundException;
import com.example.crash.model.entity.UserEntity;
import com.example.crash.model.user.User;
import com.example.crash.model.user.UserSignUpRequestBody;
import com.example.crash.repository.UserEntityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUsername(username)
                .orElseThrow(() ->new UserNotFoundException(username));
    }

    // 회원가입
    public User signUp(@Valid UserSignUpRequestBody userSignUpRequestBody) {

        // 회원가입 한적이 있는지 검증
        //ifPresent = Optional 객체가 값을 포함하고 있을 때(isPresent()가 true일 때) 주어진 작업을 수행합니다.값이 없는 경우(isPresent()가 false일 때) 아무 작업도 수행하지 않습니다
        userEntityRepository
                .findByUsername(userSignUpRequestBody.username())
                .ifPresent(
                        user -> {
                            throw new UserAlreadyExistsException();
                        });

        //회원정보 저장
        var userEntity =
                userEntityRepository.save(
                        UserEntity.of(
                                userSignUpRequestBody.username(),
                                passwordEncoder.encode(userSignUpRequestBody.password()),
                                userSignUpRequestBody.name(),
                                userSignUpRequestBody.email()));

        return User.from(userEntity);
    }
}
