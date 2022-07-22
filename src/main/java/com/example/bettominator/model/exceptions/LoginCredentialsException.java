package com.example.bettominator.model.exceptions;

public class LoginCredentialsException extends RuntimeException{
    public LoginCredentialsException(){
        super("Username or password is wrong");
    }
}
