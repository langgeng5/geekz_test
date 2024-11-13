package com.test.geekz.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.test.geekz.helper.ResponseDto;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        ResponseDto<Boolean> response = new ResponseDto<>();
        response.setStatus(false);
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.getErrorMessage().add(e.getMessage());
        response.setMessage(e.getCustomMessage());
        response.setPayload(false);
        return ResponseEntity.badRequest().body(response);
    }
}
