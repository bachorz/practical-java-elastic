package com.course.practicaljavaelastic.common;

import com.course.practicaljavaelastic.api.response.ErrorResponse;
import com.course.practicaljavaelastic.exception.IllegalApiParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MyGlobalExceptionHandler.class);

    @ExceptionHandler(value = IllegalApiParamException.class)
    private ResponseEntity<ErrorResponse> handleIllegalApiParamException(IllegalApiParamException e){
        String message = "Exception API param from MyGlobalExceptionHandler, " + e.getMessage();
        LOG.warn(message);
        ErrorResponse errorResponse = new ErrorResponse(message, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
