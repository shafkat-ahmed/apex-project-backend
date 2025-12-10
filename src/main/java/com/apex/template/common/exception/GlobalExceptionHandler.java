package com.apex.template.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class,NotFoundException.class,
    NullPasswordException.class, StorageException.class, StorageFileNotFoundException.class,
    UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        // Create a structured response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getErrorMessage()));
    }

    public static class ErrorResponse {
        private String message;
        private String page;

        public ErrorResponse(String message, String page) {
            this.message = message;
            this.page = page;
        }

        public ErrorResponse(String message) {
            this.message = message;
        }

        // Getters and Setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }
    }
}


