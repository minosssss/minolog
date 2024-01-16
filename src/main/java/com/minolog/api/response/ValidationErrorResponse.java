package com.minolog.api.response;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {
    private final List<FieldError> fieldErrors = new ArrayList<>();

    public void addFieldError(String field, String message) {
        FieldError error = new FieldError(field, message);
        this.fieldErrors.add(error);
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public static class FieldError {
        private final String field;
        private final String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        // Getters
        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}