package com.elearning.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    private int statusCode;
    private String message;

    public AppException(String message) {
        super(message);
        this.message = message;
        this.statusCode = 400;
    }

    public AppException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.statusCode = 400;
    }
}
