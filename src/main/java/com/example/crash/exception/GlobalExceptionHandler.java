package com.example.crash.exception;

import com.example.crash.model.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 여기서 처리 할 수 있게 해준다
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientErrorException.class) //ClientErrorException 이발생할 때 여기서 가로챈다
    public ResponseEntity<ClientErrorException> handlerClientErrorException(ClientErrorException e){
        return new ResponseEntity<>(
            new ClientErrorException(e.getStatus(),e.getMessage()),
                e.getStatus()
        );
    }

    @ExceptionHandler(RuntimeException.class) //RuntimeException 이발생할 때 여기서 가로챈다
    public ResponseEntity<ErrorResponse> handlerRuntimeException(RuntimeException e){
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(Exception.class) //Exception 이발생할 때 여기서 가로챈다
    public ResponseEntity<ErrorResponse> handlerException(Exception e){
        return ResponseEntity.internalServerError().build();
    }
}
