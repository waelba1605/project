package com.elearning.exception;

public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue), 404);
    }

    public ResourceNotFoundException(String message) {
        super(message, 404);
    }
}
