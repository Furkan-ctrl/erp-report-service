package com.erp.report_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReportException extends RuntimeException {
    private final HttpStatus status;

    public ReportException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}