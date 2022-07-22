package com.example.bettominator.model.exceptions;

public class CategoryNameException extends RuntimeException{
    public CategoryNameException(){
        super("Please enter category name");
    }
}
