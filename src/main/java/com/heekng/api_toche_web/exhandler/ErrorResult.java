package com.heekng.api_toche_web.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResult {

    private HttpStatus status;
    private String code;
    private String message;

}
