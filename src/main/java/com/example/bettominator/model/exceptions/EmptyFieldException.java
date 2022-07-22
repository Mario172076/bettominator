package com.example.bettominator.model.exceptions;

public class EmptyFieldException extends RuntimeException{
    public EmptyFieldException (){
        super("Please fill out all the fields");
    }
}
