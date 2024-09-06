package com.example.VisitorManagementSystem.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {
    private Integer httpStatus;
    private String exception;
    private String message;
}
