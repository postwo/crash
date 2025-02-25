package com.example.crash.exception.user;

import com.example.crash.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ClientErrorException {

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User not Found");
    }

    public UserNotFoundException(String username) {
        super(HttpStatus.NOT_FOUND, "User with username"+ username +"not Found");
    }

}
