package com.heekng.api_toche_web.exhandler.advice;

import com.heekng.api_toche_web.exhandler.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;

@RestControllerAdvice("com.heekng.api_toche_web.api")
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        return new ErrorResult(HttpStatus.BAD_REQUEST, "BAD Request", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResult validBindExHandler(BindException e) {
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bindingResult.getFieldErrorCount(); i++) {
            FieldError error = bindingResult.getFieldErrors().get(i);
            stringBuilder.append(error.getField());
            stringBuilder.append(": ");
            stringBuilder.append(error.getDefaultMessage());
            if (i < bindingResult.getFieldErrorCount() - 1) {
                stringBuilder.append(", ");
            }
        }
        return new ErrorResult(HttpStatus.BAD_REQUEST, "BAD Request", stringBuilder.toString());
    }

}
