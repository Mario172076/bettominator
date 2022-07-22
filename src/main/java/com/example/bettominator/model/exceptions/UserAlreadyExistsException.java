package com.example.bettominator.model.exceptions;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(){
        super("User with that username, email or phone number already exists");
    }
}
