package com.len.leave.manual.exception;

/**
 * Exception thrown when an invalid state transition is attempted
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (Persero)
 */
public class InvalidStateTransitionException extends RuntimeException {
    
    public InvalidStateTransitionException(String message) {
        super(message);
    }

    public InvalidStateTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
