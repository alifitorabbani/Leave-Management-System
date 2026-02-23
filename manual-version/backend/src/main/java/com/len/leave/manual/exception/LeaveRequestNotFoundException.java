package com.len.leave.manual.exception;

/**
 * Exception thrown when a leave request is not found
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (Persero)
 */
public class LeaveRequestNotFoundException extends RuntimeException {
    
    public LeaveRequestNotFoundException(String message) {
        super(message);
    }

    public LeaveRequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
