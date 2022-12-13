package com.efsauto.erste_schritte.exception;

import com.efsauto.erste_schritte.models.ToDo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.ZonedDateTime;


@ControllerAdvice // to tell spring that this class will be used for more exception handling
public class ToDoExceptionHandler {


//        @ExceptionHandler(value = {ToDoException.class}) // to tell spring that this is for handling ToDoException
    public ResponseEntity<?> handleToDoException(ToDoException ex) {


        HttpStatus httpStatus = ex.getHttpStatus();
        String errorCode = String.valueOf(httpStatus.value());

        // create Payload containing exception details
        ToDoExceptionPayload toDoExceptionPayload = new ToDoExceptionPayload(
                ex.getMessage(),
                errorCode,
                httpStatus,
                ZonedDateTime.now()

        );

        return new ResponseEntity<>(toDoExceptionPayload, httpStatus);

    }
}
