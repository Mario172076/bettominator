package com.example.bettominator.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CountryIsUsedException extends RuntimeException{
    public CountryIsUsedException(Long id){
        super(String.format("Country with %d id is used", id));
    }
}
