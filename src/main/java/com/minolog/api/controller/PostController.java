package com.minolog.api.controller;

import com.minolog.api.request.PostCreate;
import com.minolog.api.response.ValidationErrorResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public ResponseEntity<?> post(@RequestBody @Valid PostCreate params, BindingResult result) {
        if (result.hasErrors()) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse();
            for (FieldError fieldError : result.getFieldErrors()) {
                errorResponse.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorResponse);
        }

        log.info("params={}", params.toString());
        return ResponseEntity.ok("Hello World");
    }
}
