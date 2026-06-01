package com.snapBuy.project.exceptions;

/* * Generic custom exception class used for handling business logic related errors.
*  Example:
* * - Category already exists
* * - Product quantity cannot be negative
 */

public class APIException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public APIException() {
    }

    /* * Constructor used to pass custom exception message. */

    public APIException(String message) {
        super(message);
    }



}
