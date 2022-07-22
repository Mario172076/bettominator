package com.example.bettominator.model.exceptions;

public class ContactParametarsException extends RuntimeException{
    public ContactParametarsException (){
        super("Please enter all required fields");
    }
}
