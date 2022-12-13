package com.efsauto.erste_schritte.exception;

import org.springframework.http.HttpStatus;

public class WeatherException extends ToDoException{


    public WeatherException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause, httpStatus);
    }

    public WeatherException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
