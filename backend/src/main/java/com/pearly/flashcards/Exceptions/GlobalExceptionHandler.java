package com.pearly.flashcards.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pearly.flashcards.Error.dto.ErrorDto;

import jakarta.persistence.EntityNotFoundException;

// INFO: General exception handler for RestController. Custom exceptions would be better. But it would be an overkill in this project.

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleCardExceptions(Exception ex) {
        ErrorDto error = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException(NullPointerException ex) {
        ErrorDto error = new ErrorDto(HttpStatus.BAD_REQUEST.value(), "Null pointer exception: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDto error = new ErrorDto(HttpStatus.BAD_REQUEST.value(), "Illegal argument exception: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorDto error = new ErrorDto(HttpStatus.NOT_FOUND.value(), "Entity not found: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
