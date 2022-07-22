package com.example.bettominator.model.exceptions;

public class CountryNameException extends RuntimeException{
    public CountryNameException(){
        super("Please enter Country name");
    }
}
