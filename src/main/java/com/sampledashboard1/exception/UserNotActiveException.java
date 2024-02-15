package com.sampledashboard1.exception;

public class UserNotActiveException extends RuntimeException{
    public UserNotActiveException(final String message) {
        super(message);
    }
}
