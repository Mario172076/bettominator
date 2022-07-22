package com.example.bettominator.model.exceptions;

public class CountryNotFoundException extends RuntimeException{

    public CountryNotFoundException(Long id){
        super(String.format("Country with %d id is not found", id));
    }

    public CountryNotFoundException(String name) {
        super(String.format("Country with %s name is not found", name));
    }
}
