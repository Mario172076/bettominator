package com.example.bettominator.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException{
    public GameNotFoundException(Long id) {
        super(String.format("Product with id: %d was not found", id));
    }
}
