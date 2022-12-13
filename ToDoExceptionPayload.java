package com.efsauto.erste_schritte.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ToDoExceptionPayload {
    private final String message;
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timeStamp;


    public ToDoExceptionPayload(String message, String errorCode, HttpStatus httpStatus, ZonedDateTime timeStamp) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }
}
