package com.efsauto.erste_schritte.exception;

import org.springframework.http.HttpStatus;

public class ElasticSearchException extends ToDoException {


    public ElasticSearchException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause, httpStatus);
    }

    public ElasticSearchException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
