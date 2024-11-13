package com.test.geekz.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ResponseDto<T> {
    private Boolean status = true;
    private Integer code = HttpStatus.OK.value();
    private List<String> errorMessage = new ArrayList<>();
    private String message;
    private T payload;
}