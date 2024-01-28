package com.minolog.api.controller;

import com.minolog.api.exception.CommonException;
import com.minolog.api.exception.PostNotFound;
import com.minolog.api.response.ValidErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ValidErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ValidErrorResponse response = ValidErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }

//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(PostNotFound.class)
//    @ResponseBody
//    public ValidErrorResponse postNotFound(PostNotFound e) {
//
//        return ValidErrorResponse.builder()
//                .code("404")
//                .message(e.getMessage())
//                .build();
//    }

    @ResponseBody
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ValidErrorResponse> MinoLogException(CommonException e) {
        int statusCode = e.getStatusCode();

        ValidErrorResponse body = ValidErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(statusCode)
                .body(body);
    }
}
