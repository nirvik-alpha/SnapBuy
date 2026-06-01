package com.snapBuy.project.exceptions;


/*
* Custom exception used when a requested resource cannot be found in the database.
*
* Example:
* - Product not found with id: 5
* - User not found with email: abc@gmail.com
* * Extending RuntimeException allows us to throw this exception without mandatory try-catch blocks.
*/

public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    /// Constructors

    public ResourceNotFoundException() {
    }

    // resource lookup with fieldName
    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    // resource lookup with fieldId
    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }


}
