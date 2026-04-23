package com.elearning.exception;

public class DuplicateResourceException extends AppException {
    public DuplicateResourceException(String message) {
        super(message, 409);
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue), 409);
    }
}
