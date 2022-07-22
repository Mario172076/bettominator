package com.example.bettominator.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class CategoryIsUsedException extends RuntimeException{
    public CategoryIsUsedException(Long id){
        super(String.format("Category with %d id is used", id));
    }
}
