package br.edu.atitus.api_citytour.controllers.advice;

import br.edu.atitus.api_citytour.components.ResourceNotFoundExcep;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(ResourceNotFoundExcep.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundExcep ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
