package com.ssafy.commitmood.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?>  handleException(Exception e) {
        log.error(e.getMessage(), e.getStackTrace(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error 로그 참고");
    }
}
