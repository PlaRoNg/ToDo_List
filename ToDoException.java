package com.efsauto.erste_schritte.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for toDo errors.
 */
public class ToDoException extends Exception {

    private static final long serialVersionUID = 8707947232585487078L;

    private HttpStatus httpStatus;

    // constructor ###############################
    public ToDoException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public ToDoException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    // getter and setter ################################

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
