package com.project.ddm.exception;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException(String msg) {
        super(msg);
    }
}
