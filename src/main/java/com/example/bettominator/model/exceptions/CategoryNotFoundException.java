package com.example.bettominator.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(Long id){
        super(String.format("Category with %d id is not found", id));
    }

    public CategoryNotFoundException(String name) {
        super(String.format("Category with %s name is not found", name));
    }
}
