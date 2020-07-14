package com.joanna.hotel.controller;

import com.joanna.hotel.exception.NoRoomsAvailableException;
import com.joanna.hotel.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = NoRoomsAvailableException.class)
    public ResponseEntity<JsonResponse> handleNoRoomsAvailableException() {
        return new ResponseEntity<>(new JsonResponse("No rooms available to book"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<JsonResponse> handleResourceNotFoundException() {
        return new ResponseEntity<>(new JsonResponse("Resource not found"), HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {

        return handleExceptionInternal(ex, new JsonResponse(ex.getMessage()), headers, HttpStatus.BAD_REQUEST, request);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private class JsonResponse {
        String message;
    }
}
