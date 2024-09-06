package com.example.VisitorManagementSystem.exception;

public class BadRequestException  extends RuntimeException{

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}
