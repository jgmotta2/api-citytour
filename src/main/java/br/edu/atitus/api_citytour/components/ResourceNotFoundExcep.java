package br.edu.atitus.api_citytour.components;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundExcep extends Exception {

    public ResourceNotFoundExcep(String message) {
        super(message);
    }
}
