package com.ecommerce.ashluxe.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String fieldName;
    String field;
    Long fieldId;

    public ResourceNotFoundException( String field, String fieldName, String resourceName) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, field));
        this.field = field;
        this.fieldName = fieldName;
        this.resourceName = resourceName;
    }
    public ResourceNotFoundException( String field, String resourceName, Long fieldId) {
        super(String.format("%s not found with %s : '%d'", resourceName, field, fieldId));
        this.fieldId = fieldId;
        this.field = field;
        this.resourceName = resourceName;
    }

    public ResourceNotFoundException() {
    }
}
