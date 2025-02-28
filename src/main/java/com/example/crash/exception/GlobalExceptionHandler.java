package com.example.crash.exception;

import com.example.crash.model.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    // 데이터가 하나라도 비어있을때 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        var errorMessage =
                e.getFieldErrors().stream()
                        .map(fieldError -> (fieldError.getField() + ": " + fieldError.getDefaultMessage()))
                        .toList()
                        .toString();

        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
    }

    // body값이 하나도 없을때 발생
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST, "Required request body is missing."),
                HttpStatus.BAD_REQUEST);
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
