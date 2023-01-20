package com.project.ddm.exception;

public class OrderNotExistException  extends RuntimeException{
    public OrderNotExistException(String message) {
        super(message);
    }
}
